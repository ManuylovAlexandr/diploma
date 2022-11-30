package com.project.port.adapter.semantic.changes.entity;

import javax.annotation.Nonnull;

import com.project.port.adapter.semantic.OntologyManager;

public class TermQueryChanges extends BaseProjectEntityChanges<TermQueryChanges>
{

    public TermQueryChanges(
        @Nonnull OntologyManager ontologyManager,
        @Nonnull String abbreviatedIRI
    )
    {
        super(ontologyManager, abbreviatedIRI);
    }

    @Override
    protected TermQueryChanges getThis()
    {
        return this;
    }
}
