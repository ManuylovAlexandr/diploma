package com.project.port.adapter.semantic.projection;

import static java.util.stream.Collectors.toSet;

import com.project.domain.event.StoredEventDeserializer;
import com.project.domain.treatmentoption.TreatmentOptionComponent;
import com.project.domain.treatmentoption.event.NewTreatmentOptionAdded;
import com.project.domain.treatmentoption.event.TreatmentOptionAlternativeNamesChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionCanonicalNameChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionComponentsChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionDescriptionChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionRelationToComponentsChanged;
import com.project.domain.classification.CaseInsensitiveName;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.entity.TreatmentOptionChanges;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class TreatmentOptionSemanticMaintainer extends SemanticMaintainer
{

    public TreatmentOptionSemanticMaintainer(OntologyManager ontologyManager, StoredEventDeserializer deserializer)
    {
        super(deserializer, ontologyManager);

        registerHandler(NewTreatmentOptionAdded.class, this::onNewTreatmentOptionAdded);
        registerHandler(TreatmentOptionCanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(TreatmentOptionAlternativeNamesChanged.class, this::onAlternativeNamesChanged);
        registerHandler(TreatmentOptionDescriptionChanged.class, this::onDescriptionChanged);
        registerHandler(TreatmentOptionRelationToComponentsChanged.class, this::onRelationToComponentsChanged);
        registerHandler(TreatmentOptionComponentsChanged.class, this::onComponentsChanged);
    }

    private TreatmentOptionChanges onNewTreatmentOptionAdded(NewTreatmentOptionAdded event)
    {
        return this.changesConstructor(event.treatmentOptionId().asText())
            .setSuperClass(OwlClassesVocabulary.TREATMENT_OPTION.abbreviatedIRI())
            .setProjectId(event.treatmentOptionId().asText())
            .setCanonicalName(event.canonicalName().asText())
            .setMetadata(event.metadata())
            .setComponents(
                event.components().stream().map(TreatmentOptionComponent.Id::identity).collect(toSet()),
                event.relationToComponents()
            );
    }

    private TreatmentOptionChanges onCanonicalNameChanged(TreatmentOptionCanonicalNameChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeCanonicalName(event.canonicalName().asText())
            .changeMetadata(event.metadata());
    }

    private TreatmentOptionChanges onAlternativeNamesChanged(TreatmentOptionAlternativeNamesChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeSynonyms(event.alternativeNames().stream().map(CaseInsensitiveName::asText).collect(toSet()))
            .changeMetadata(event.metadata());
    }

    private TreatmentOptionChanges onDescriptionChanged(TreatmentOptionDescriptionChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeDescription(event.description().orElse(""))
            .changeMetadata(event.metadata());
    }

    private TreatmentOptionChanges onComponentsChanged(TreatmentOptionComponentsChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeComponents(event.components().stream().map(TreatmentOptionComponent.Id::identity).collect(toSet()))
            .changeMetadata(event.metadata());
    }

    private TreatmentOptionChanges onRelationToComponentsChanged(TreatmentOptionRelationToComponentsChanged event)
    {
        return this.changesConstructor(event.identity())
            .changeComponentsRelation(event.relationToComponents())
            .changeMetadata(event.metadata());
    }

    private TreatmentOptionChanges changesConstructor(String abbreviatedIRI)
    {
        return new TreatmentOptionChanges(this.ontologyManager, abbreviatedIRI);
    }

    @Override
    public void reset()
    {
        // nop
    }
}
