package com.project.port.adapter.semantic.projection;

import javax.annotation.Nonnull;

import com.project.domain.classification.radiotherapy.event.NewRadiotherapyAdded;
import com.project.domain.classification.radiotherapy.event.RadiotherapyAlternativeNamesChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyCanonicalNameChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyDescriptionChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyParentClassChanged;
import com.project.domain.event.StoredEventDeserializer;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.entity.DrugChanges;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class RadiotherapySemanticMaintainer extends SemanticMaintainer
{

    public RadiotherapySemanticMaintainer(OntologyManager ontologyManager, StoredEventDeserializer deserializer)
    {
        super(deserializer, ontologyManager);

        registerHandler(NewRadiotherapyAdded.class, this::onNewRadiotherapyAdded);
        registerHandler(RadiotherapyCanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(RadiotherapyAlternativeNamesChanged.class, this::onAlternativeNamesChanged);
        registerHandler(RadiotherapyDescriptionChanged.class, this::onDescriptionChanged);
        registerHandler(RadiotherapyParentClassChanged.class, this::onParentClassChanged);
    }

    private DrugChanges onNewRadiotherapyAdded(@Nonnull NewRadiotherapyAdded event)
    {
        return this.changesConstructor(event.identity())
            .setSuperClass(OwlClassesVocabulary.UNCLASSIFIED_RADIOTHERAPIES.abbreviatedIRI())
            .setProjectId(event.identity())
            .setCanonicalName(event.canonicalName())
            .setMetadata(event.metadata());
    }

    private DrugChanges onCanonicalNameChanged(@Nonnull RadiotherapyCanonicalNameChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeCanonicalName(event.canonicalName())
            .setMetadata(event.metadata());
    }

    private DrugChanges onAlternativeNamesChanged(@Nonnull RadiotherapyAlternativeNamesChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSynonyms(event.alternativeNames())
            .changeMetadata(event.metadata());
    }

    private DrugChanges onDescriptionChanged(RadiotherapyDescriptionChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeDescription(event.description().orElse(""))
            .changeMetadata(event.metadata());
    }

    private DrugChanges onParentClassChanged(@Nonnull RadiotherapyParentClassChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSuperClass(event.parentClass()
                .orElse(OwlClassesVocabulary.UNCLASSIFIED_RADIOTHERAPIES.abbreviatedIRI()))
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
