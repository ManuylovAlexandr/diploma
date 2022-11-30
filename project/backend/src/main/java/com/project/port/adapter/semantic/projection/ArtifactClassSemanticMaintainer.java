package com.project.port.adapter.semantic.projection;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactClassTypeTag;
import com.project.domain.classification.therapyclass.event.AlternativeNamesChanged;
import com.project.domain.classification.therapyclass.event.CanonicalNameChanged;
import com.project.domain.classification.therapyclass.event.DescriptionChanged;
import com.project.domain.classification.therapyclass.event.NewAdded;
import com.project.domain.classification.therapyclass.event.SuperClassChanged;
import com.project.domain.event.StoredEventDeserializer;
import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.CaseInsensitiveName;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.entity.ArtifactClassChanges;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class ArtifactClassSemanticMaintainer extends SemanticMaintainer
{

    private static final Set<String> RESERVED_ROOT_CLASS_IDS = Arrays.stream(ArtifactClassTypeTag.values())
        .map(ArtifactClassTypeTag::typeTagClassId)
        .map(AggregateRootId::asText)
        .collect(toSet());

    public ArtifactClassSemanticMaintainer(OntologyManager ontologyManager, StoredEventDeserializer deserializer)
    {
        super(deserializer, ontologyManager);

        registerHandler(NewAdded.class, this::onNewClassAdded);
        registerHandler(CanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(DescriptionChanged.class, this::onDescriptionChanged);
        registerHandler(SuperClassChanged.class, this::onSuperClassChanged);
        registerHandler(AlternativeNamesChanged.class, this::onAlternativeNamesChanged);
    }

    private ArtifactClassChanges onNewClassAdded(@Nonnull NewAdded event)
    {
        var changes = this.constructBuilder(event.identity().asText())
            .setProjectId(event.identity().asText())
            .setMetadata(event.metadata())
            .setSuperClasses(event.superClass()
                .map(AggregateRootId::asText)
                .orElse(OwlClassesVocabulary.ARTIFACT_CLASS.abbreviatedIRI()));

        if (!RESERVED_ROOT_CLASS_IDS.contains(event.identity().asText()))
        {
            changes = changes.setCanonicalName(event.canonicalName().asText());
        }

        return changes;
    }

    private ArtifactClassChanges onCanonicalNameChanged(@Nonnull CanonicalNameChanged event)
    {

        return this.constructBuilder(event.identity().asText())
            .changeCanonicalName(event.canonicalName().asText())
            .changeMetadata(event.metadata());
    }

    private ArtifactClassChanges onDescriptionChanged(DescriptionChanged event)
    {
        return this.constructBuilder(event.identity().asText())
            .changeDescription(event.description().orElse(""))
            .changeMetadata(event.metadata());
    }

    private ArtifactClassChanges onSuperClassChanged(SuperClassChanged event)
    {
        return this.constructBuilder(event.identity().asText())
            .changeSuperClasses(event.superClass().asText())
            .changeMetadata(event.metadata());
    }

    private ArtifactClassChanges onAlternativeNamesChanged(AlternativeNamesChanged event)
    {
        return this.constructBuilder(event.identity().asText())
            .changeSynonyms(event.alternativeNames().stream().map(CaseInsensitiveName::asText).collect(toSet()))
            .changeMetadata(event.metadata());
    }

    private ArtifactClassChanges constructBuilder(String abbreviatedIRI)
    {
        return new ArtifactClassChanges(this.ontologyManager, abbreviatedIRI);
    }

    @Override
    public void reset()
    {
        // nop
    }
}
