package com.project.port.adapter.semantic.reasoner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

public class ProtegeOwlEntityChecker extends ShortFormEntityChecker
{

    public ProtegeOwlEntityChecker(BidirectionalShortFormProvider shortFormProvider)
    {
        super(shortFormProvider);
    }

    @Nullable
    @Override
    public OWLClass getOWLClass(@Nonnull String name)
    {
        return super.getOWLClass(stripAndEscapeRendering(name));
    }

    @Nullable
    @Override
    public OWLDataProperty getOWLDataProperty(@Nonnull String name)
    {
        return super.getOWLDataProperty(stripAndEscapeRendering(name));
    }

    @Nullable
    @Override
    public OWLDatatype getOWLDatatype(@Nonnull String name)
    {
        return super.getOWLDatatype(stripAndEscapeRendering(name));
    }

    @Nullable
    @Override
    public OWLNamedIndividual getOWLIndividual(@Nonnull String name)
    {
        return super.getOWLIndividual(stripAndEscapeRendering(name));
    }

    @Nullable
    @Override
    public OWLObjectProperty getOWLObjectProperty(@Nonnull String name)
    {
        return super.getOWLObjectProperty(stripAndEscapeRendering(name));
    }

    @Nullable
    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull String name)
    {
        return super.getOWLAnnotationProperty(stripAndEscapeRendering(name));
    }

    private static String stripAndEscapeRendering(String rendering)
    {
        String strippedRendering;
        if (rendering.startsWith("'") && rendering.endsWith("'"))
        {
            strippedRendering = rendering.substring(1, rendering.length() - 1);
        } else
        {
            strippedRendering = rendering;
        }
        return strippedRendering;
    }
}
