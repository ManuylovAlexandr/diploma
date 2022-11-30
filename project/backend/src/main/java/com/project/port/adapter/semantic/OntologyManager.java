package com.project.port.adapter.semantic;

import static java.util.Collections.emptyMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AnnotationValueShortFormProvider;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class OntologyManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(OntologyManager.class);

    private final OWLOntologyManager manager;

    private OWLOntology ontology;

    private final OWLDataFactory dataFactory;

    private final PrefixManager defaultPrefixManager;

    private OWLReasoner reasoner;

    private AnnotationValueShortFormProvider shortFormProvider;

    private org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter bidirectionalShortFormProviderAdapter;

    public OntologyManager()
    {
        this.manager = OWLManager.createOWLOntologyManager();
        this.dataFactory = this.manager.getOWLDataFactory();
        this.defaultPrefixManager = new DefaultPrefixManager(Namespaces.PREFIX.namespace());
    }

    public OWLOntology ontology()
    {
        return this.ontology;
    }

    public void createOntology()
    {
        try
        {
            this.ontology = this.manager.createOntology(IRI.create(Namespaces.SOURCE.namespace()));
        }
        catch (OWLOntologyCreationException e)
        {
            LOGGER.error("Cannot create ontology", e);
        }
        this.reasoner = new ReasonerFactory().createReasoner(this.ontology);
        this.shortFormProvider = new AnnotationValueShortFormProvider(
            List.of(this.dataFactory.getRDFSLabel()),
            emptyMap(),
            this.manager
        );
        this.bidirectionalShortFormProviderAdapter = new BidirectionalShortFormProviderAdapter(
            this.manager, this.manager.getOntologies(), this.shortFormProvider
        );
    }

    public void applyChanges(List<OWLOntologyChange> changes)
    {
        this.manager().applyChanges(changes);
    }

    public OWLDataFactory dataFactory()
    {
        return this.dataFactory;
    }

    public PrefixManager defaultPrefixManager()
    {
        return this.defaultPrefixManager;
    }

    public OWLOntologyManager manager()
    {
        return this.manager;
    }

    public OWLReasoner reasoner()
    {
        return this.reasoner;
    }

    public ShortFormProvider shortFormProvider()
    {
        return this.shortFormProvider;
    }

    public org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter bidirectionalShortFormProviderAdapter()
    {
        return this.bidirectionalShortFormProviderAdapter;
    }

    public OWLClass asOwlClass(String abbreviatedIRI)
    {
        return this.dataFactory()
            .getOWLClass(encodeIRI(abbreviatedIRI), this.defaultPrefixManager());
    }

    private static String encodeIRI(String iri)
    {
        return URLEncoder.encode(iri, StandardCharsets.UTF_8);
    }
}
