package com.project.port.adapter.semantic.changes.property;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.changes.AbstractChanges;

public class AnnotationPropertiesChanges extends AbstractChanges
{

    private final OWLDataFactory dataFactory;

    private final OWLProperty annotationProperty;

    public AnnotationPropertiesChanges(
        OntologyManager ontologyManager,
        HasIRI property
    )
    {
        super(ontologyManager);
        this.dataFactory = ontologyManager.dataFactory();
        this.annotationProperty = this.dataFactory.getOWLAnnotationProperty(property);
    }

    public AnnotationPropertiesChanges withAnnotationPropertyDomainAxiom()
    {
        this.addAxiom(this.dataFactory.getOWLAnnotationPropertyDomainAxiom(
            this.annotationProperty.asOWLAnnotationProperty(),
            this.dataFactory.getTopDatatype().getIRI()
        ));
        return this;
    }

    public AnnotationPropertiesChanges withAnnotationPropertyRangeAxiom(OWL2Datatype datatype)
    {
        this.addAxiom(this.dataFactory.getOWLAnnotationPropertyRangeAxiom(
            this.annotationProperty.asOWLAnnotationProperty(),
            datatype.getIRI()
        ));
        return this;
    }

    public AnnotationPropertiesChanges withCanonicalName(String canonicalName)
    {
        this.addAxiom(this.dataFactory.getOWLAnnotationAssertionAxiom(
            this.dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()),
            this.annotationProperty.getIRI(),
            this.dataFactory.getOWLLiteral(canonicalName)
        ));
        return this;
    }
}
