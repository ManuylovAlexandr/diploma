package com.project.port.adapter.semantic.projection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.semanticweb.owlapi.reasoner.InferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.project.application.ProjectionMaintainer;
import com.project.domain.event.StoredEvent;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.common.projections.ProjectionMaintainingStoredEventHandler;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.management.RDFVersionManager;

@Component
@ConditionalOnProperty(name = "project.semantic-engine.enabled")
public class SemanticProjectionMaintainer implements ProjectionMaintainer
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticProjectionMaintainer.class);

    public static final String PROJECTION_NAME = "prjn-rdf-store";

    private final TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    private final OntologyManager ontologyManager;

    private final RDFVersionManager versionManager;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final AtomicLong projectionVersion = new AtomicLong(0);

    private final List<ProjectionMaintainingStoredEventHandler> eventHandlers;

    private final TermQuerySemanticMaintainer termQuerySemanticMaintainer;

    private final ArtifactClassSemanticMaintainer artifactClassSemanticMaintainer;

    protected SemanticProjectionMaintainer(
        DataSource dataSource,
        @ProjectionMaintainerComponent(projectionName = PROJECTION_NAME)
            List<ProjectionMaintainingStoredEventHandler> eventHandlers,
        TaskScheduler taskScheduler,
        OntologyManager ontologyManager,
        RDFVersionManager versionManager
    )
    {
        this.taskScheduler = taskScheduler;
        this.ontologyManager = ontologyManager;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.eventHandlers = eventHandlers;
        this.versionManager = versionManager;
        this.termQuerySemanticMaintainer = findMaintainerOrThrow(TermQuerySemanticMaintainer.class);
        this.artifactClassSemanticMaintainer = findMaintainerOrThrow(ArtifactClassSemanticMaintainer.class);
    }

    @PostConstruct
    public void schedule()
    {
        this.scheduledFuture = this.taskScheduler.schedule(this::updateProjection, new CronTrigger("*/2 * * * * *"));
    }

    public void updateProjection()
    {
        var eventCounter = new AtomicLong(0);
        try (var eventStream = this.jdbcTemplate.queryForStream(
            """
                select e.id,
                       e.stream_id,
                       e.occurred_on,
                       e.event_type,
                       e.event_payload::text,
                       e.event_metadata::text
                from (select s.*, rank() over (partition by event_type, stream_id order by id desc) rnk
                        from stored_events s
                       where s.id > :lastSeenEventId) e
                where e.rnk = 1
                order by e.id
                """,
            Map.of("lastSeenEventId", this.projectionVersion.get()),
            (ResultSet row, int rowNum) -> asStoredEvent(row)
        ))
        {
            eventStream
                .forEach((StoredEvent storedEvent) -> {
                    this.handleEvent(storedEvent);
                    this.projectionVersion.getAndSet(storedEvent.id());
                });

            if (eventCounter.get() != 0L)
            {
                flushChangesAndSyncInference();
                if (this.termQuerySemanticMaintainer.alignEquivalenceClassesForTerms())
                {
                    flushChangesAndSyncInference();
                }
            }
        }
    }

    @Nonnull
    public Optional<Long> lastSeenEventId()
    {
        return Optional.of(this.projectionVersion.get());
    }

    public void resetProjection()
    {
        this.scheduledFuture.cancel(true);
        this.ontologyManager.createOntology();
        this.versionManager.migrate();
        this.schedule();
    }

    @Override
    public String projectionName()
    {
        return PROJECTION_NAME;
    }

    protected void handleEvent(StoredEvent storedEvent)
    {
        this.eventHandlers.forEach((ProjectionMaintainingStoredEventHandler eventHandler) -> {
            try
            {
                eventHandler.handleEvent(storedEvent);
            }
            catch (Exception ex)
            {
                LOGGER.info("Exception during updating projection. Event id {}", storedEvent.id(), ex);
            }
        });
    }

    public boolean isAsync()
    {
        return true;
    }

    private void flushChangesAndSyncInference()
    {
        this.ontologyManager.reasoner().flush();
        this.ontologyManager.reasoner().precomputeInferences(InferenceType.values());
    }

    @Nonnull
    private <E extends SemanticMaintainer> E findMaintainerOrThrow(@Nonnull Class<E> maintainerClass)
    {
        return this.eventHandlers.stream()
            .filter(maintainerClass::isInstance)
            .findFirst()
            .map(maintainerClass::cast)
            .orElseThrow();
    }

    private static StoredEvent asStoredEvent(ResultSet row)
        throws SQLException
    {
        return StoredEvent.builder()
            .withId(row.getLong("id"))
            .withStreamId(row.getString("stream_id"))
            .occurredOn(row.getObject("occurred_on", OffsetDateTime.class))
            .withEventTypeName(row.getString("event_type"))
            .withEventPayload(row.getString("event_payload"))
            .withEventMetadataPayload(row.getString("event_metadata"))
            .build();
    }
}
