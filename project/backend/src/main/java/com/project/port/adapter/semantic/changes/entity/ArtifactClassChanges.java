package com.project.port.adapter.semantic.changes.entity;

import javax.annotation.Nonnull;

import com.project.port.adapter.semantic.OntologyManager;

public class ArtifactClassChanges extends BaseProjectEntityChanges<ArtifactClassChanges>
{

    public ArtifactClassChanges(
        @Nonnull OntologyManager ontologyManager,
        @Nonnull String abbreviatedIRI
    )
    {
        super(ontologyManager, abbreviatedIRI);
    }

    public ArtifactClassChanges setSuperClasses(@Nonnull String superClasses)
    {
        this.setSuperClass(superClasses);
        return getThis();
    }

    public ArtifactClassChanges changeSuperClasses(@Nonnull String superClasses)
    {
        this.owlChangesBuilder.removeSubClassOfAxiom(
            owlAxiom -> owlAxiom.getSubClass().isClassExpressionLiteral()
        );
        this.setSuperClass(superClasses);
        return getThis();
    }

    @Override
    public ArtifactClassChanges getThis()
    {
        return this;
    }
}
