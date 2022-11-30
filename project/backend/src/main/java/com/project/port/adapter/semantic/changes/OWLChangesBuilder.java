package com.project.port.adapter.semantic.changes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.visitors.AnnotationAssertionVisitor;
import com.project.port.adapter.semantic.visitors.AxiomVisitor;
import com.project.port.adapter.semantic.visitors.ForRemoveVisitor;

public class OWLChangesBuilder extends AbstractChanges
{

    public final OWLClass owlClass;

    public final OWLDataFactory dataFactory;

    public OWLChangesBuilder(OntologyManager ontologyManager, String entityId)
    {
        super(ontologyManager);
        this.dataFactory = ontologyManager.dataFactory();
        this.owlClass = asOwlClass(entityId);
    }

    @Nonnull
    public OWLChangesBuilder addSubClassOfAxiom(
        @Nonnull String abbreviatedIRI
    )
    {
        return this.addSubClassOfAxiom(this.asOwlClass(abbreviatedIRI));
    }

    @Nonnull
    public OWLChangesBuilder addSubClassOfAxiom(
        @Nonnull Function<OWLDataFactory, OWLClassExpression> axiomFactory
    )
    {
        Optional.ofNullable(axiomFactory.apply(this.dataFactory))
            .ifPresent(this::addSubClassOfAxiom);
        return this;
    }

    @Nonnull
    public OWLChangesBuilder addSubClassOfAxiom(
        @Nonnull OWLClassExpression owlClassExpression
    )
    {
        this.addAxiom(this.dataFactory.getOWLSubClassOfAxiom(this.owlClass, owlClassExpression));
        return this;
    }

    @Nonnull
    public OWLChangesBuilder addEquivalentClassesAxiom(
        @Nonnull OWLClassExpression owlClassExpression
    )
    {
        this.addAxiom(this.dataFactory.getOWLEquivalentClassesAxiom(this.owlClass, owlClassExpression));
        return this;
    }

    @Nonnull
    public OWLChangesBuilder addAnnotationAssertionAxiom(
        @Nonnull HasIRI annotationProperty,
        @Nonnull String value
    )
    {
        return this.addAnnotationAssertionAxiom(annotationProperty, value, OWL2Datatype.XSD_STRING);
    }

    @Nonnull
    public OWLChangesBuilder addAnnotationAssertionAxiom(
        @Nonnull HasIRI annotationProperty,
        @Nonnull Collection<String> stringStream,
        @Nonnull OWL2Datatype datatype
    )
    {
        stringStream.forEach(s -> this.addAnnotationAssertionAxiom(annotationProperty, s, datatype));
        return this;
    }

    @Nonnull
    public OWLChangesBuilder addAnnotationAssertionAxiom(
        @Nonnull HasIRI annotationProperty,
        @Nonnull String value,
        @Nonnull OWL2Datatype datatype
    )
    {
        this.addAxiom(
            this.dataFactory.getOWLAnnotationAssertionAxiom(
                this.dataFactory.getOWLAnnotationProperty(annotationProperty),
                this.owlClass.getIRI(),
                this.dataFactory.getOWLLiteral(value, datatype)
            )
        );
        return this;
    }

    @Nonnull
    public OWLChangesBuilder removeAnnotationAssertionAxiom(
        @Nonnull HasIRI annotationProperty
    )
    {
        var visitor = new AnnotationAssertionVisitor(annotationProperty);
        this.ontologyManager.ontology().annotationAssertionAxioms(this.owlClass.getIRI()).forEach(visitor::visit);
        visitor.getAxiomsForRemove().forEach(this::removeAxiom);
        return this;
    }

    @Nonnull
    public OWLChangesBuilder changeAnnotationAssertionAxiom(
        @Nonnull HasIRI annotationProperty,
        @Nonnull String value
    )
    {
        return this.changeAnnotationAssertionAxiom(annotationProperty, value, OWL2Datatype.XSD_STRING);
    }

    @Nonnull
    public OWLChangesBuilder changeAnnotationAssertionAxiom(
        @Nonnull HasIRI annotationProperty,
        @Nonnull String value,
        @Nonnull OWL2Datatype owl2Datatype
    )
    {
        return this.removeAnnotationAssertionAxiom(annotationProperty)
            .addAnnotationAssertionAxiom(annotationProperty, value, owl2Datatype);
    }

    public OWLChangesBuilder removeSubClassOfAxiom(@Nonnull Predicate<OWLSubClassOfAxiom> predicateForSearchAxiom)
    {
        return this.removeSubClassOfAxiom(new AxiomVisitor(predicateForSearchAxiom));
    }

    public OWLChangesBuilder removeSubClassOfAxiom(@Nonnull ForRemoveVisitor<OWLSubClassOfAxiom> visitor)
    {
        this.ontologyManager.ontology()
            .subClassAxiomsForSubClass(this.owlClass)
            .forEach(visitor::visit);
        visitor.getAxiomsForRemove().forEach(this::removeAxiom);
        return this;
    }

    @Nonnull
    public OWLChangesBuilder changeSubClassOfAxiom(
        @Nonnull Predicate<OWLSubClassOfAxiom> predicateForSearchAxiom,
        @Nonnull Function<OWLDataFactory, OWLClassExpression> axiomFactory
    )
    {
        return this.removeSubClassOfAxiom(predicateForSearchAxiom)
            .addSubClassOfAxiom(axiomFactory);
    }

    public OWLChangesBuilder addDeclarationAxiom()
    {
        this.addAxiom(this.dataFactory.getOWLDeclarationAxiom(this.owlClass));
        return this;
    }

    public OWLChangesBuilder addDisjointClassesAxiom(List<OWLClass> owlClassExpression)
    {
        this.addAxiom(this.dataFactory.getOWLDisjointClassesAxiom(owlClassExpression));
        return this;
    }

    public OWLChangesBuilder addAxiom(Function<OWLDataFactory, OWLAxiom> factory)
    {
        Optional.ofNullable(factory.apply(this.dataFactory))
            .ifPresent(this::addAxiom);
        return this;
    }

    public OWLChangesBuilder addAxioms(Function<OWLDataFactory, List<OWLAxiom>> factory)
    {
        factory.apply(this.dataFactory).forEach(this::addAxiom);
        return this;
    }

    public OWLChangesBuilder removeAxiom(Function<OWLDataFactory, OWLAxiom> factory)
    {
        Optional.ofNullable(factory.apply(this.dataFactory))
            .ifPresent(this::removeAxiom);
        return this;
    }

    public final OWLClass asOwlClass(String abbreviatedIRI)
    {
        return this.ontologyManager.asOwlClass(abbreviatedIRI);
    }

    @Nonnull
    public OWLDataFactory dataFactory()
    {
        return this.dataFactory;
    }
}
