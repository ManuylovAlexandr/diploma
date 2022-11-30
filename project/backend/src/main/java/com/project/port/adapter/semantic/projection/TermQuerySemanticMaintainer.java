package com.project.port.adapter.semantic.projection;

import static java.util.Collections.emptyList;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.project.domain.event.StoredEventDeserializer;
import com.project.domain.termquery.TermQueryRepository;
import com.project.domain.termquery.events.TermQueryCanonicalNameChanged;
import com.project.domain.termquery.events.TermQueryQueryChanged;
import com.project.domain.termquery.events.NewTermQueryAdded;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassPropertyValueProvider;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.OwlVocabulary;
import com.project.port.adapter.semantic.changes.entity.TermQueryChanges;
import com.project.port.adapter.semantic.reasoner.DLQueryParser;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class TermQuerySemanticMaintainer extends SemanticMaintainer
{

    private final DLQueryParser dlQueryParser;

    private final TermQueryRepository termQueryRepository;

    private final OwlClassPropertyValueProvider owlClassPropertyValueProvider;

    public TermQuerySemanticMaintainer(
        OntologyManager ontologyManager,
        StoredEventDeserializer deserializer,
        DLQueryParser dlQueryParser,
        TermQueryRepository termQueryRepository,
        OwlClassPropertyValueProvider owlClassPropertyValueProvider
    )
    {
        super(deserializer, ontologyManager);

        this.dlQueryParser = dlQueryParser;
        this.termQueryRepository = termQueryRepository;
        this.owlClassPropertyValueProvider = owlClassPropertyValueProvider;

        registerHandler(NewTermQueryAdded.class, this::onNewHLTAdded);
        registerHandler(TermQueryCanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(TermQueryQueryChanged.class, this::onQueryChanged);
    }

    private TermQueryChanges onNewHLTAdded(@Nonnull NewTermQueryAdded event)
    {
        return this.changesConstructor(event.id().asText())
            .setSuperClass(OwlClassesVocabulary.TQ.abbreviatedIRI())
            .setProjectId(event.id().asText())
            .setCanonicalName(event.canonicalName().asText())
            .setMetadata(event.metadata());
    }

    private TermQueryChanges onCanonicalNameChanged(@Nonnull TermQueryCanonicalNameChanged event)
    {
        return this.changesConstructor(event.id().asText())
            .changeCanonicalName(event.canonicalName().asText())
            .changeMetadata(event.metadata());
    }

    private TermQueryChanges onQueryChanged(@Nonnull TermQueryQueryChanged event)
    {
        return this.changesConstructor(event.id().asText())
            .changeMetadata(event.metadata());
    }

    private TermQueryChanges changesConstructor(String abbreviatedIRI)
    {
        return new TermQueryChanges(this.ontologyManager, abbreviatedIRI);
    }

    @Override
    public void reset()
    {
        // nop
    }

    public boolean alignEquivalenceClassesForTerms()
    {
        var additionalChanges = this.termQueryRepository.all().stream()
            .filter(termQuery -> this.owlClassPropertyValueProvider.hltQuery(
                        this.ontologyManager.asOwlClass(termQuery.id().asText())
                    )
                    .map(termQuery.query()::equals)
                    .orElse(Boolean.TRUE)
            )
            .map(termQuery ->
                this.dlQueryParser.maybeParseClassExpression(termQuery.query())
                    .map(eqClassExpression ->
                        changesConstructor(termQuery.id().asText())
                            .changesBuilder()
                            .addEquivalentClassesAxiom(eqClassExpression)
                            .changeAnnotationAssertionAxiom(OwlVocabulary.HLT_QUERY, termQuery.query())
                            .getChanges()
                    )
                    .orElse(emptyList())
            )
            .flatMap(Collection::stream)
            .toList();

        if (additionalChanges.isEmpty())
        {
            return false;
        }

        this.ontologyManager.applyChanges(additionalChanges);
        return true;
    }
}
