package com.project.port.adapter.semantic.migrations;

import org.semanticweb.owlapi.model.HasIRI;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.OwlVocabulary;
import com.project.port.adapter.semantic.changes.property.ObjectPropertiesChanges;
import com.project.port.adapter.semantic.management.RDFMigration;
import com.project.port.adapter.semantic.management.RDFVersion;

@RDFVersion(version = "V2022_02_28_4__Init_object_properties")
public class InitObjectProperties implements RDFMigration
{

    @Override
    public void migrate(OntologyManager ontologyManager)
    {
        var artifactClass = ontologyManager.asOwlClass(OwlClassesVocabulary.ARTIFACT_CLASS.abbreviatedIRI());
        var treatmentOption = ontologyManager.asOwlClass(OwlClassesVocabulary.TREATMENT_OPTION.abbreviatedIRI());

        builder(ontologyManager, OwlVocabulary.HAS_COMPONENT)
            .withCanonicalName(OwlVocabulary.HAS_COMPONENT.shortName())
            .withObjectPropertyDomainAxiom(treatmentOption)
            .withObjectPropertyRangeAxiom(dataFactory -> dataFactory.getOWLObjectUnionOf(
                artifactClass,
                treatmentOption
            ))
            .getChanges(ontologyManager::applyChanges);
    }

    private static ObjectPropertiesChanges builder(OntologyManager ontologyManager, HasIRI property)
    {
        return new ObjectPropertiesChanges(ontologyManager, property);
    }
}
