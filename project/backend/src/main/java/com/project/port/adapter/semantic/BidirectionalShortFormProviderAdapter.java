package com.project.port.adapter.semantic;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidirectionalShortFormProviderAdapter extends org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter
{

    private static final Logger LOGGER = LoggerFactory.getLogger(BidirectionalShortFormProviderAdapter.class);

    public BidirectionalShortFormProviderAdapter(
        OWLOntologyManager man,
        Collection<OWLOntology> ontologies,
        ShortFormProvider shortFormProvider
    )
    {
        super(man, ontologies, shortFormProvider);
    }

    @Override
    protected String generateShortForm(@Nonnull OWLEntity entity)
    {
        var shortForm = super.generateShortForm(entity);
        if (LOGGER.isDebugEnabled() && entity instanceof OWLClass owlClass)
        {
            LOGGER.debug("Short form for [{}] -> [{}]", owlClass.getIRI(), shortForm);
        }
        return shortForm;
    }
}
