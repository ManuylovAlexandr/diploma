package com.project.port.adapter.semantic.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class AxiomVisitor implements ForRemoveVisitor<OWLSubClassOfAxiom>
{

    private final Predicate<OWLSubClassOfAxiom> predicate;

    private final List<OWLSubClassOfAxiom> axioms = new ArrayList<>();

    public AxiomVisitor(Predicate<OWLSubClassOfAxiom> predicate)
    {
        this.predicate = predicate;
    }

    @Override
    public Boolean visit(@Nonnull OWLSubClassOfAxiom axiom)
    {
        if (this.predicate.test(axiom))
        {
            this.axioms.add(axiom);
            return true;
        }
        return false;
    }

    public List<OWLSubClassOfAxiom> getAxiomsForRemove()
    {
        return this.axioms;
    }
}
