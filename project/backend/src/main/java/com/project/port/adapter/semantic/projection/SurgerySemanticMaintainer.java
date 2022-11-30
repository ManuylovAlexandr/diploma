package com.project.port.adapter.semantic.projection;

import javax.annotation.Nonnull;

import com.project.domain.classification.surgery.event.NewSurgeryAdded;
import com.project.domain.classification.surgery.event.SurgeryAlternativeNamesChanged;
import com.project.domain.classification.surgery.event.SurgeryCanonicalNameChanged;
import com.project.domain.classification.surgery.event.SurgeryDescriptionChanged;
import com.project.domain.classification.surgery.event.SurgeryParentClassChanged;
import com.project.domain.event.StoredEventDeserializer;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.entity.DrugChanges;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class SurgerySemanticMaintainer extends SemanticMaintainer
{

    public SurgerySemanticMaintainer(OntologyManager ontologyManager, StoredEventDeserializer deserializer)
    {
        super(deserializer, ontologyManager);

        registerHandler(NewSurgeryAdded.class, this::onNewSurgeryAdded);
        registerHandler(SurgeryCanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(SurgeryAlternativeNamesChanged.class, this::onAlternativeNamesChanged);
        registerHandler(SurgeryDescriptionChanged.class, this::onDescriptionChanged);
        registerHandler(SurgeryParentClassChanged.class, this::onParentClassChanged);
    }

    private DrugChanges onNewSurgeryAdded(@Nonnull NewSurgeryAdded event)
    {
        return this.changesConstructor(event.identity())
            .setSuperClass(OwlClassesVocabulary.UNCLASSIFIED_SURGERIES.abbreviatedIRI())
            .setProjectId(event.identity())
            .setCanonicalName(event.canonicalName())
            .setMetadata(event.metadata());
    }

    private DrugChanges onCanonicalNameChanged(@Nonnull SurgeryCanonicalNameChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeCanonicalName(event.canonicalName())
            .setMetadata(event.metadata());
    }

    private DrugChanges onAlternativeNamesChanged(@Nonnull SurgeryAlternativeNamesChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSynonyms(event.alternativeNames())
            .changeMetadata(event.metadata());
    }

    private DrugChanges onDescriptionChanged(SurgeryDescriptionChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeDescription(event.description().orElse(""))
            .changeMetadata(event.metadata());
    }

    private DrugChanges onParentClassChanged(@Nonnull SurgeryParentClassChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSuperClass(event.parentClass()
                .orElse(OwlClassesVocabulary.UNCLASSIFIED_SURGERIES.abbreviatedIRI()))
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
