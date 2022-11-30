package com.project.port.adapter.semantic.projection;

import static java.util.stream.Collectors.toSet;

import javax.annotation.Nonnull;

import com.project.domain.classification.drug.Drug;
import com.project.domain.classification.drug.event.DrugAlternativeNamesChanged;
import com.project.domain.classification.drug.event.DrugCanonicalNameChanged;
import com.project.domain.classification.drug.event.DrugDescriptionChanged;
import com.project.domain.classification.drug.event.DrugTherapyClassChanged;
import com.project.domain.classification.drug.event.NewDrugAdded;
import com.project.domain.event.StoredEventDeserializer;
import com.project.domain.classification.AggregateRootId;
import com.project.port.adapter.common.projections.ProjectionMaintainerComponent;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.entity.DrugChanges;

@ProjectionMaintainerComponent(projectionName = SemanticProjectionMaintainer.PROJECTION_NAME)
public class DrugSemanticMaintainer extends SemanticMaintainer
{

    public DrugSemanticMaintainer(OntologyManager ontologyManager, StoredEventDeserializer deserializer)
    {
        super(deserializer, ontologyManager);

        registerHandler(NewDrugAdded.class, this::onNewDrugAdded);
        registerHandler(DrugCanonicalNameChanged.class, this::onCanonicalNameChanged);
        registerHandler(DrugAlternativeNamesChanged.class, this::onAlternativeNamesChanged);
        registerHandler(DrugDescriptionChanged.class, this::onDescriptionChanged);
        registerHandler(DrugTherapyClassChanged.class, this::onTherapyClassChanged);
    }

    private DrugChanges onNewDrugAdded(@Nonnull NewDrugAdded event)
    {
        return this.changesConstructor(event.drugId().asText())
            .setSuperClass(OwlClassesVocabulary.UNCLASSIFIED_DRUGS.abbreviatedIRI())
            .setProjectId(event.drugId().asText())
            .setCanonicalName(event.canonicalName().asText())
            .setMetadata(event.metadata());
    }

    private DrugChanges onCanonicalNameChanged(@Nonnull DrugCanonicalNameChanged event)
    {
        return this.changesConstructor(event.drugId().asText())
            .changeCanonicalName(event.canonicalName().asText())
            .setMetadata(event.metadata());
    }

    private DrugChanges onAlternativeNamesChanged(@Nonnull DrugAlternativeNamesChanged event)
    {
        return this.changesConstructor(event.drugId().asText())
            .changeSynonyms(event.alternativeNames().stream().map(Drug.Name::asText).collect(toSet()))
            .changeMetadata(event.metadata());
    }

    private DrugChanges onDescriptionChanged(DrugDescriptionChanged event)
    {
        return this.changesConstructor(event.drugId().asText())
            .changeDescription(event.description().orElse(""))
            .changeMetadata(event.metadata());
    }

    private DrugChanges onTherapyClassChanged(@Nonnull DrugTherapyClassChanged event)
    {
        return this.changesConstructor(event.drugId().asText())
            .changeSuperClass(event.therapyClass()
                .map(AggregateRootId::asText)
                .orElse(OwlClassesVocabulary.UNCLASSIFIED_DRUGS.abbreviatedIRI()))
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
