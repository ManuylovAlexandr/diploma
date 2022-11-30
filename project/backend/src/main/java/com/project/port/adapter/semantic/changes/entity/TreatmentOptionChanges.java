package com.project.port.adapter.semantic.changes.entity;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNaryBooleanClassExpression;

import com.project.domain.treatmentoption.TreatmentOptionRelationToComponents;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.OwlVocabulary;
import com.project.port.adapter.semantic.visitors.TreatmentOptionVisitor;

public class TreatmentOptionChanges extends BaseProjectEntityChanges<TreatmentOptionChanges>
{

    public TreatmentOptionChanges(
        @Nonnull OntologyManager ontologyManager,
        @Nonnull String abbreviatedIRI)
    {
        super(ontologyManager, abbreviatedIRI);
    }

    public TreatmentOptionChanges setComponents(
        @Nonnull Set<String> components,
        @Nonnull TreatmentOptionRelationToComponents relation)
    {
        this.owlChangesBuilder.addSubClassOfAxiom(owlDataFactory -> componentsAxiomWithComponentRelation(
            components.stream()
                .map(componentId -> owlDataFactory.getOWLObjectSomeValuesFrom(
                    owlDataFactory.getOWLObjectProperty(OwlVocabulary.HAS_COMPONENT),
                    this.owlChangesBuilder.asOwlClass(componentId)
                )).toList(),
            relation
        ).apply(owlDataFactory));
        return this;
    }

    public TreatmentOptionChanges changeComponents(@Nonnull Set<String> components)
    {
        var visitor = new TreatmentOptionVisitor();
        this.owlChangesBuilder.removeSubClassOfAxiom(visitor);
        this.owlChangesBuilder.addSubClassOfAxiom(owlDataFactory -> componentsAxiomWithComponentRelation(
            components.stream()
                .map(componentId -> owlDataFactory.getOWLObjectSomeValuesFrom(
                    owlDataFactory.getOWLObjectProperty(OwlVocabulary.HAS_COMPONENT),
                    this.owlChangesBuilder.asOwlClass(componentId)
                )).toList(),
            visitor.getRelation()
        ).apply(owlDataFactory));
        return this;
    }

    public TreatmentOptionChanges changeComponentsRelation(@Nonnull TreatmentOptionRelationToComponents relation)
    {
        var visitor = new TreatmentOptionVisitor();
        this.owlChangesBuilder.removeSubClassOfAxiom(visitor);
        this.owlChangesBuilder.addSubClassOfAxiom(owlDataFactory -> componentsAxiomWithComponentRelation(
            visitor.getComponents(),
            relation
        ).apply(owlDataFactory));
        return this;
    }

    public static Function<OWLDataFactory, OWLNaryBooleanClassExpression> componentsAxiomWithComponentRelation(
        Collection<? extends OWLClassExpression> components,
        TreatmentOptionRelationToComponents componentsRelation
    )
    {
        return dataFactory -> switch (componentsRelation)
            {
                case HAS_PART -> dataFactory.getOWLObjectIntersectionOf(components);
                case SUBSUMES -> dataFactory.getOWLObjectUnionOf(components);
            };
    }

    @Override
    protected TreatmentOptionChanges getThis()
    {
        return this;
    }
}
