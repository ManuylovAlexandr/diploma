package com.project.port.adapter.semantic.visitors;

import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;

public interface ForRemoveVisitor<E extends OWLAxiom> extends OWLAxiomVisitorEx<Boolean>
{

    List<E> getAxiomsForRemove();
}
