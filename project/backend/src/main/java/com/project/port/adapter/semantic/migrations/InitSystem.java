package com.project.port.adapter.semantic.migrations;

import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.OWLChangesBuilder;
import com.project.port.adapter.semantic.management.RDFMigration;
import com.project.port.adapter.semantic.management.RDFVersion;

@RDFVersion(version = "V2022_02_28_1__Init_system")
public class InitSystem implements RDFMigration
{

    @Override
    public void migrate(OntologyManager ontologyManager)
    {
        builder(ontologyManager, OwlClassesVocabulary.SYSTEM.abbreviatedIRI())
            .addAnnotationAssertionAxiom(OWLRDFVocabulary.RDFS_LABEL, OwlClassesVocabulary.SYSTEM.getName())
            .addDeclarationAxiom()
            .getChanges(ontologyManager::applyChanges);
    }

    private static OWLChangesBuilder builder(OntologyManager ontologyManager, String entityId)
    {
        return new OWLChangesBuilder(ontologyManager, entityId);
    }
}
