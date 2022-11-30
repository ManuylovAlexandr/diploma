package com.project.port.adapter.semantic.changes.property;

import java.util.function.Function;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.changes.AbstractChanges;

public class ObjectPropertiesChanges extends AbstractChanges
{

    private final OWLDataFactory dataFactory;

    private final OWLObjectProperty objectProperty;

    public ObjectPropertiesChanges(
        OntologyManager ontologyManager,
        HasIRI property
    )
    {
        super(ontologyManager);
        this.dataFactory = ontologyManager.dataFactory();
        this.objectProperty = this.dataFactory.getOWLObjectProperty(property);
    }

    public ObjectPropertiesChanges withObjectPropertyRangeAxiom(
        OWLClassExpression owlClassExpression)
    {
        this.addAxiom(this.dataFactory.getOWLObjectPropertyRangeAxiom(
            this.objectProperty,
            owlClassExpression
        ));
        return this;
    }

    public ObjectPropertiesChanges withObjectPropertyRangeAxiom(
        Function<OWLDataFactory, OWLClassExpression> owlClassExpressionFactory)
    {
        this.addAxiom(this.dataFactory.getOWLObjectPropertyRangeAxiom(
            this.objectProperty,
            owlClassExpressionFactory.apply(this.dataFactory)
        ));
        return this;
    }

    public ObjectPropertiesChanges withObjectPropertyDomainAxiom(OWLClassExpression owlClassExpression)
    {
        this.addAxiom(this.dataFactory.getOWLObjectPropertyDomainAxiom(
            this.objectProperty,
            owlClassExpression
        ));
        return this;
    }

    public ObjectPropertiesChanges withInverseFunctionalObjectPropertyAxiom()
    {
        this.dataFactory.getOWLInverseFunctionalObjectPropertyAxiom(this.objectProperty);
        return this;
    }

    public ObjectPropertiesChanges withFunctionalObjectPropertyAxiom()
    {
        this.dataFactory.getOWLFunctionalObjectPropertyAxiom(this.objectProperty);
        return this;
    }

    public ObjectPropertiesChanges withInverseObjectPropertiesAxiom(HasIRI inverseProperty)
    {
        this.dataFactory.getOWLInverseObjectPropertiesAxiom(
            this.objectProperty,
            this.dataFactory.getOWLObjectProperty(inverseProperty)
        );
        return this;
    }

    public ObjectPropertiesChanges withCanonicalName(String canonicalName)
    {
        this.addAxiom(this.dataFactory.getOWLAnnotationAssertionAxiom(
            this.dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()),
            this.objectProperty.getIRI(),
            this.dataFactory.getOWLLiteral(canonicalName)
        ));
        return this;
    }
}
