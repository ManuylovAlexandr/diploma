package com.project.port.adapter.semantic.migrations;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlVocabulary;
import com.project.port.adapter.semantic.changes.property.AnnotationPropertiesChanges;
import com.project.port.adapter.semantic.management.RDFMigration;
import com.project.port.adapter.semantic.management.RDFVersion;

@RDFVersion(version = "V2022_02_28_3__Init_annotation_properties")
public class InitAnnotationProperties implements RDFMigration
{

    @Override
    public void migrate(OntologyManager ontologyManager)
    {
        builder(ontologyManager, OwlVocabulary.CODE).withAnnotationPropertyRangeAxiom(OWL2Datatype.XSD_STRING)
            .withAnnotationPropertyDomainAxiom()
            .withCanonicalName(OwlVocabulary.CODE.shortName())
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlVocabulary.SYNONYM).withAnnotationPropertyRangeAxiom(OWL2Datatype.XSD_STRING)
            .withAnnotationPropertyDomainAxiom()
            .withCanonicalName(OwlVocabulary.SYNONYM.shortName())
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlVocabulary.DESCRIPTION).withAnnotationPropertyRangeAxiom(OWL2Datatype.XSD_STRING)
            .withAnnotationPropertyDomainAxiom()
            .withCanonicalName(OwlVocabulary.DESCRIPTION.shortName())
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlVocabulary.EQUIVALENCE_CLASS)
            .withAnnotationPropertyRangeAxiom(OWL2Datatype.XSD_BOOLEAN)
            .withAnnotationPropertyDomainAxiom()
            .withCanonicalName(OwlVocabulary.EQUIVALENCE_CLASS.shortName())
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlVocabulary.CREATED_AT).withAnnotationPropertyRangeAxiom(OWL2Datatype.XSD_LONG)
            .withAnnotationPropertyDomainAxiom()
            .withCanonicalName(OwlVocabulary.CREATED_AT.shortName())
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlVocabulary.HLT_QUERY).withAnnotationPropertyRangeAxiom(OWL2Datatype.XSD_STRING)
            .withAnnotationPropertyDomainAxiom()
            .withCanonicalName(OwlVocabulary.HLT_QUERY.shortName())
            .getChanges(ontologyManager::applyChanges);
    }

    private static AnnotationPropertiesChanges builder(OntologyManager ontologyManager, HasIRI property)
    {
        return new AnnotationPropertiesChanges(ontologyManager, property);
    }
}
