package com.project.port.adapter.semantic.visitors;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

public class AnnotationAssertionVisitor implements ForRemoveVisitor<OWLAnnotationAssertionAxiom>
{

    private final HasIRI annotationProperty;

    private final List<OWLAnnotationAssertionAxiom> axioms = new ArrayList<>();

    public AnnotationAssertionVisitor(HasIRI annotationProperty)
    {
        this.annotationProperty = annotationProperty;
    }

    @Override
    public Boolean visit(OWLAnnotationAssertionAxiom axiom)
    {
        if (axiom.getAnnotation().getProperty().getIRI().equals(this.annotationProperty.getIRI()))
        {
            this.axioms.add(axiom);
            return true;
        }
        return false;
    }

    @Override
    public List<OWLAnnotationAssertionAxiom> getAxiomsForRemove()
    {
        return this.axioms;
    }
}
