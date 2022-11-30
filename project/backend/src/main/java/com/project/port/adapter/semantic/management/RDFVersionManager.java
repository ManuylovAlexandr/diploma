package com.project.port.adapter.semantic.management;

import static java.util.Comparator.comparing;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.project.common.Pair;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlClassesVocabulary;
import com.project.port.adapter.semantic.changes.OWLChangesBuilder;

@Component
@ConditionalOnProperty(name = "project.semantic-engine.enabled")
public class RDFVersionManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RDFVersionManager.class);

    private final OntologyManager ontologyManager;

    private final List<RDFMigration> migrations;

    public RDFVersionManager(
        OntologyManager ontologyManager,
        List<RDFMigration> migrations
    )
    {
        this.ontologyManager = ontologyManager;
        this.migrations = migrations;
    }

    @PostConstruct
    public void init()
    {
        this.migrate();
    }

    @Nonnull
    private Optional<String> currentVersion()
    {
        var property = this.ontologyManager.ontology()
            .annotationAssertionAxioms(
                this.ontologyManager.asOwlClass(OwlClassesVocabulary.SYSTEM.abbreviatedIRI()).getIRI()
            )
            .filter(owlAnnotationAssertionAxiom -> owlAnnotationAssertionAxiom.getAnnotation()
                .getProperty()
                .getIRI()
                .equals(
                    OWLRDFVocabulary.OWL_VERSION_INFO.getIRI()))
            .findFirst();

        return property.map(OWLAnnotationAssertionAxiom::annotationValue)
            .map(OWLAnnotationValue::asLiteral)
            .flatMap(owlLiteral -> owlLiteral.map(OWLLiteral::getLiteral));
    }

    private void updateVersion(String version)
    {
        new OWLChangesBuilder(this.ontologyManager, OwlClassesVocabulary.SYSTEM.abbreviatedIRI())
            .changeAnnotationAssertionAxiom(OWLRDFVocabulary.OWL_VERSION_INFO, version)
            .getChanges(this.ontologyManager::applyChanges);
    }

    public void migrate()
    {
        this.ontologyManager.createOntology();
        var current = this.currentVersion().orElse("");
        LOGGER.info("Loaded {} migrations. Current version {}", this.migrations.size(), current);
        var newMigrations = this.migrations.stream()
            .map(rdfMigration -> new Pair<>(
                rdfMigration.getClass().getAnnotation(RDFVersion.class).version(),
                rdfMigration
            ))
            .sorted(comparing(Pair::left))
            .filter(rdfMigrationPair -> rdfMigrationPair.left().compareTo(current) > 0)
            .toList();

        var versionKeeper = new AtomicReference<>("");

        if (!newMigrations.isEmpty())
        {
            LOGGER.info("Found {} not applied migrations", newMigrations.size());
            newMigrations.forEach((Pair<String, RDFMigration> rdfMigrationPair) -> {
                rdfMigrationPair.right().migrate(this.ontologyManager);
                LOGGER.info(
                    "Migration {} are successfully applied",
                    versionKeeper.updateAndGet(s -> rdfMigrationPair.left())
                );
            });

            this.updateVersion(versionKeeper.get());
        }
    }
}
