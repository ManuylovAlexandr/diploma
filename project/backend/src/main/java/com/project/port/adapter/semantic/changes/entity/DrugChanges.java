package com.project.port.adapter.semantic.changes.entity;

import java.util.Set;

import javax.annotation.Nonnull;

import com.project.port.adapter.semantic.OntologyManager;

public class DrugChanges extends BaseProjectEntityChanges<DrugChanges>
{

    public DrugChanges(
        @Nonnull OntologyManager ontologyManager,
        @Nonnull String abbreviatedIRI)
    {
        super(ontologyManager, abbreviatedIRI);
    }

    public DrugChanges setSuperClasses(@Nonnull Set<String> superClasses)
    {
        superClasses.forEach(this::setSuperClass);
        return getThis();
    }

    public DrugChanges changeSuperClasses(@Nonnull Set<String> superClasses)
    {
        this.owlChangesBuilder.removeSubClassOfAxiom(
            owlAxiom -> owlAxiom.getSubClass().isClassExpressionLiteral()
        );
        superClasses.forEach(this::setSuperClass);
        return getThis();
    }

    @Override
    protected DrugChanges getThis()
    {
        return this;
    }
}
