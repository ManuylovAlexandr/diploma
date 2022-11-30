package com.project.port.adapter.semantic.migrations;

import java.util.List;
import java.util.Map;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.OwlVocabulary;
import com.project.port.adapter.semantic.changes.OWLChangesBuilder;
import com.project.port.adapter.semantic.changes.entity.ArtifactClassChanges;
import com.project.port.adapter.semantic.management.RDFMigration;
import com.project.port.adapter.semantic.management.RDFVersion;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

@RDFVersion(version = "V2022_02_28_2__Init_default_classes")
public class InitDefaultClasses implements RDFMigration
{

    @Override
    public void migrate(OntologyManager ontologyManager)
    {
        builder(ontologyManager, OwlClassesVocabulary.THERAPY.abbreviatedIRI())
            .addAnnotationAssertionAxiom(OWLRDFVocabulary.RDFS_LABEL, OwlClassesVocabulary.THERAPY.getName())
            .addDeclarationAxiom()
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlClassesVocabulary.ARTIFACT_TYPE.abbreviatedIRI())
            .addAnnotationAssertionAxiom(
                OWLRDFVocabulary.RDFS_LABEL,
                OwlClassesVocabulary.ARTIFACT_TYPE.getName()
            )
            .addDeclarationAxiom()
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlClassesVocabulary.ARTIFACT_CLASS.abbreviatedIRI())
            .addAnnotationAssertionAxiom(OWLRDFVocabulary.RDFS_LABEL, OwlClassesVocabulary.ARTIFACT_CLASS.getName())
            .addDeclarationAxiom()
            .addSubClassOfAxiom(OwlClassesVocabulary.THERAPY.abbreviatedIRI())
            .getChanges(ontologyManager::applyChanges);

        builder(ontologyManager, OwlClassesVocabulary.TREATMENT_OPTION.abbreviatedIRI())
            .addAnnotationAssertionAxiom(
                OWLRDFVocabulary.RDFS_LABEL,
                OwlClassesVocabulary.TREATMENT_OPTION.getName()
            )
            .addSubClassOfAxiom(OwlClassesVocabulary.THERAPY.abbreviatedIRI())
            .addDeclarationAxiom()
            .getChanges(ontologyManager::applyChanges);

        var rootTreeClasses = List.of(
            OwlClassesVocabulary.DRUGS,
            OwlClassesVocabulary.PLACEBOS,
            OwlClassesVocabulary.SURGERIES,
            OwlClassesVocabulary.RADIOTHERAPIES
        );
        rootTreeClasses.stream()
            .map(root -> builder(ontologyManager, root.abbreviatedIRI())
                .addAnnotationAssertionAxiom(OwlVocabulary.CODE, root.abbreviatedIRI())
                .addAnnotationAssertionAxiom(OWLRDFVocabulary.RDFS_LABEL, root.getName())
                .addSubClassOfAxiom(OwlClassesVocabulary.ARTIFACT_CLASS.abbreviatedIRI())
                .addDisjointClassesAxiom(
                    rootTreeClasses.stream()
                        .map(OwlClassesVocabulary::abbreviatedIRI).map(ontologyManager::asOwlClass)
                        .toList()
                )
            )
            .map(OWLChangesBuilder::getChanges)
            .forEach(ontologyManager::applyChanges);

        Map.of(
            OwlClassesVocabulary.UNCLASSIFIED_DRUGS, OwlClassesVocabulary.DRUGS,
            OwlClassesVocabulary.UNCLASSIFIED_PLACEBOS, OwlClassesVocabulary.PLACEBOS,
            OwlClassesVocabulary.UNCLASSIFIED_RADIOTHERAPIES, OwlClassesVocabulary.RADIOTHERAPIES,
            OwlClassesVocabulary.UNCLASSIFIED_SURGERIES, OwlClassesVocabulary.SURGERIES
        ).forEach((unclassified, rootClass) -> artifactClassBuilder(ontologyManager, unclassified.abbreviatedIRI())
            .setProjectId(unclassified.abbreviatedIRI())
            .setCanonicalName(unclassified.getName())
            .setSuperClass(rootClass.abbreviatedIRI())
            .getChanges(ontologyManager::applyChanges)
        );

        builder(ontologyManager, OwlClassesVocabulary.TQ.abbreviatedIRI())
            .addAnnotationAssertionAxiom(OWLRDFVocabulary.RDFS_LABEL, OwlClassesVocabulary.TQ.getName())
            .addDeclarationAxiom()
            .getChanges(ontologyManager::applyChanges);
    }

    private static OWLChangesBuilder builder(OntologyManager ontologyManager, String entityId)
    {
        return new OWLChangesBuilder(ontologyManager, entityId);
    }

    private static ArtifactClassChanges artifactClassBuilder(OntologyManager ontologyManager, String entityId)
    {
        return new ArtifactClassChanges(ontologyManager, entityId);
    }
}
