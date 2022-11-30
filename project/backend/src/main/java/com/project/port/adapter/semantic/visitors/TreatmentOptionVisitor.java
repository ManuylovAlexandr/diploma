package com.project.port.adapter.semantic.visitors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNaryBooleanClassExpression;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import com.project.domain.treatmentoption.TreatmentOptionRelationToComponents;

public class TreatmentOptionVisitor implements ForRemoveVisitor<OWLSubClassOfAxiom>
{

    private final List<OWLClassExpression> components = new ArrayList<>();

    private TreatmentOptionRelationToComponents relation;

    @Nullable
    private OWLSubClassOfAxiom componentsAxiom;

    public List<OWLClassExpression> getComponents()
    {
        return this.components;
    }

    public TreatmentOptionRelationToComponents getRelation()
    {
        return this.relation;
    }

    @Override
    public Boolean visit(OWLSubClassOfAxiom axiom)
    {
        axiom.components()
            .filter(OWLNaryBooleanClassExpression.class::isInstance)
            .findFirst()
            .ifPresent((Object o) -> {
                switch (o)
                {
                    case OWLObjectIntersectionOf owlObjectIntersectionOf -> {
                        owlObjectIntersectionOf.operands()
                            .collect(toCollection(() -> this.components));
                        this.relation = TreatmentOptionRelationToComponents.HAS_PART;
                    }
                    case OWLObjectUnionOf objectUnionOf -> {
                        objectUnionOf.operands()
                            .collect(toCollection(() -> this.components));
                        this.relation = TreatmentOptionRelationToComponents.SUBSUMES;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + o);
                }
                this.componentsAxiom = axiom;
            });
        return this.componentsAxiom != null;
    }

    @Override
    public List<OWLSubClassOfAxiom> getAxiomsForRemove()
    {
        return Optional.ofNullable(this.componentsAxiom).map(List::of).orElse(emptyList());
    }
}
