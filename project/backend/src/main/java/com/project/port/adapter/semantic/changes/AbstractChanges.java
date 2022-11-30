package com.project.port.adapter.semantic.changes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import com.project.port.adapter.semantic.OntologyManager;

public abstract class AbstractChanges
{

    protected final List<OWLOntologyChange> changes = new ArrayList<>();

    public final OntologyManager ontologyManager;

    public AbstractChanges(OntologyManager ontologyManager)
    {
        this.ontologyManager = ontologyManager;
    }

    public void addAxiom(@Nonnull OWLAxiom axiom)
    {

        this.changes.add(new AddAxiomData(axiom).createOntologyChange(this.ontologyManager.ontology()));
    }

    public void removeAxiom(@Nonnull OWLAxiom axiom)
    {
        this.changes.add(new RemoveAxiomData(axiom).createOntologyChange(this.ontologyManager.ontology()));
    }

    @Nonnull
    public List<OWLOntologyChange> getChanges()
    {
        return this.changes;
    }

    public void getChanges(Consumer<List<OWLOntologyChange>> consumer)
    {
        consumer.accept(this.changes);
    }
}
