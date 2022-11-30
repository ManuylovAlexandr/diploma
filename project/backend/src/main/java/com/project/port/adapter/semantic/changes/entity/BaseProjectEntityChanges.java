package com.project.port.adapter.semantic.changes.entity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.project.domain.event.DomainEventMetadata;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlVocabulary;
import com.project.port.adapter.semantic.changes.OWLChangesBuilder;

public abstract class BaseProjectEntityChanges<E extends BaseProjectEntityChanges<E>>
{

    protected final OWLChangesBuilder owlChangesBuilder;

    public BaseProjectEntityChanges(OntologyManager ontologyManager, String abbreviatedIRI)
    {
        this.owlChangesBuilder = new OWLChangesBuilder(ontologyManager, abbreviatedIRI);
    }

    @Nonnull
    public E setCanonicalName(@Nonnull String canonicalName)
    {
        this.owlChangesBuilder.addAnnotationAssertionAxiom(
            OWLRDFVocabulary.RDFS_LABEL,
            canonicalName
        );
        return getThis();
    }

    public E changeCanonicalName(@Nonnull String canonicalName)
    {
        this.owlChangesBuilder.removeAnnotationAssertionAxiom(OWLRDFVocabulary.RDFS_LABEL);
        this.setCanonicalName(canonicalName);
        return getThis();
    }

    public E setProjectId(@Nonnull String id)
    {
        this.owlChangesBuilder.addAnnotationAssertionAxiom(OwlVocabulary.CODE, id);
        return getThis();
    }

    public E setMetadata(@Nonnull DomainEventMetadata metadata)
    {
        this.owlChangesBuilder.addAnnotationAssertionAxiom(
                OwlVocabulary.CREATED_AT,
                Long.toString(metadata.occurredOn().toEpochSecond()),
                OWL2Datatype.XSD_LONG
            )
            .addAnnotationAssertionAxiom(
                OWLRDFVocabulary.OWL_VERSION_INFO,
                Long.toString(metadata.revision()),
                OWL2Datatype.XSD_LONG
            );

        return getThis();
    }

    public E changeMetadata(@Nonnull DomainEventMetadata metadata)
    {
        this.owlChangesBuilder.changeAnnotationAssertionAxiom(
            OWLRDFVocabulary.OWL_VERSION_INFO,
            Long.toString(metadata.revision()),
            OWL2Datatype.XSD_LONG
        );

        return getThis();
    }

    public E setDescription(@Nonnull String description)
    {
        this.owlChangesBuilder.addAnnotationAssertionAxiom(
            OwlVocabulary.DESCRIPTION,
            description
        );

        return getThis();
    }

    public E changeDescription(@Nonnull String description)
    {
        this.owlChangesBuilder.changeAnnotationAssertionAxiom(
            OwlVocabulary.DESCRIPTION,
            description
        );

        return getThis();
    }

    public E setSuperClass(@Nonnull String superClassId)
    {
        this.owlChangesBuilder.addSubClassOfAxiom(owlDataFactory ->
            owlDataFactory.getOWLClass(
                superClassId,
                this.owlChangesBuilder.ontologyManager.defaultPrefixManager()
            )
        );
        return getThis();
    }

    public E changeSuperClass(@Nullable String superClassId)
    {
        Predicate<OWLSubClassOfAxiom> owlAxiomPredicate = owlAxiom -> owlAxiom.getSubClass()
            .isClassExpressionLiteral();
        Optional.ofNullable(superClassId).ifPresentOrElse(
            superClass -> this.owlChangesBuilder.changeSubClassOfAxiom(owlAxiomPredicate, owlDataFactory ->
                owlDataFactory.getOWLClass(
                    superClass,
                    this.owlChangesBuilder.ontologyManager.defaultPrefixManager()
                )
            ),
            () -> this.owlChangesBuilder.removeSubClassOfAxiom(owlAxiomPredicate)
        );
        return getThis();
    }

    public E setSynonyms(Set<String> synonyms)
    {
        this.owlChangesBuilder.addAnnotationAssertionAxiom(
            OwlVocabulary.SYNONYM,
            synonyms,
            OWL2Datatype.XSD_STRING
        );
        return getThis();
    }

    public E changeSynonyms(Set<String> synonyms)
    {
        this.owlChangesBuilder.removeAnnotationAssertionAxiom(OwlVocabulary.SYNONYM)
            .addAnnotationAssertionAxiom(
                OwlVocabulary.SYNONYM,
                synonyms,
                OWL2Datatype.XSD_STRING
            );
        return getThis();
    }

    public E andThen(Consumer<OWLClass> action)
    {
        action.accept(this.owlChangesBuilder.owlClass);
        return getThis();
    }

    public final void getChanges(Consumer<List<OWLOntologyChange>> consumer)
    {
        consumer.accept(this.owlChangesBuilder.getChanges());
    }

    protected abstract E getThis();

    public final OWLChangesBuilder changesBuilder()
    {
        return this.owlChangesBuilder;
    }
}
