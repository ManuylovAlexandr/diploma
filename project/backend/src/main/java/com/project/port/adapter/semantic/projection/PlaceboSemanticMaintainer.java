package com.project.port.adapter.semantic.projection;

import javax.annotation.Nonnull;

import com.project.domain.classification.placebo.event.NewPlaceboAdded;
import com.project.domain.classification.placebo.event.PlaceboAlternativeNamesChanged;
import com.project.domain.classification.placebo.event.PlaceboCanonicalNameChanged;
import com.project.domain.classification.placebo.event.PlaceboDescriptionChanged;
import com.project.domain.classification.placebo.event.PlaceboParentClassChanged;
import com.project.domain.event.StoredEventDeserializer;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.entity.DrugChanges;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class PlaceboSemanticMaintainer extends SemanticMaintainer
{

    public PlaceboSemanticMaintainer(OntologyManager ontologyManager, StoredEventDeserializer deserializer)
    {
        super(deserializer, ontologyManager);

        registerHandler(NewPlaceboAdded.class, this::onNewPlaceboAdded);
        registerHandler(PlaceboCanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(PlaceboAlternativeNamesChanged.class, this::onAlternativeNamesChanged);
        registerHandler(PlaceboDescriptionChanged.class, this::onDescriptionChanged);
        registerHandler(PlaceboParentClassChanged.class, this::onParentClassChanged);
    }

    private DrugChanges onNewPlaceboAdded(@Nonnull NewPlaceboAdded event)
    {
        return this.changesConstructor(event.identity())
            .setSuperClass(OwlClassesVocabulary.UNCLASSIFIED_PLACEBOS.abbreviatedIRI())
            .setProjectId(event.identity())
            .setCanonicalName(event.canonicalName())
            .setMetadata(event.metadata());
    }

    private DrugChanges onCanonicalNameChanged(@Nonnull PlaceboCanonicalNameChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeCanonicalName(event.canonicalName())
            .setMetadata(event.metadata());
    }

    private DrugChanges onAlternativeNamesChanged(@Nonnull PlaceboAlternativeNamesChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSynonyms(event.alternativeNames())
            .changeMetadata(event.metadata());
    }

    private DrugChanges onDescriptionChanged(PlaceboDescriptionChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeDescription(event.description().orElse(""))
            .changeMetadata(event.metadata());
    }

    private DrugChanges onParentClassChanged(@Nonnull PlaceboParentClassChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSuperClass(event.parentClass()
                .orElse(OwlClassesVocabulary.UNCLASSIFIED_PLACEBOS.abbreviatedIRI()))
            .changeMetadata(event.metadata());
    }

    private DrugChanges changesConstructor(String abbreviatedIRI)
    {
        return new DrugChanges(this.ontologyManager, abbreviatedIRI);
    }

    @Override
    public void reset()
    {
        // nop
    }
}
