package com.project.port.adapter.semantic;

import java.util.Optional;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.springframework.stereotype.Service;

@Service
public class OwlClassPropertyValueProvider
{

    private final OntologyManager ontologyManager;

    public OwlClassPropertyValueProvider(OntologyManager ontologyManager)
    {
        this.ontologyManager = ontologyManager;
    }

    public Optional<String> canonicalName(OWLClass owlClass)
    {
        return maybeAnnotationValue(owlClass, OWLRDFVocabulary.RDFS_LABEL.getIRI());
    }

    public Optional<String> id(OWLClass owlClass)
    {
        return maybeAnnotationValue(owlClass, OwlVocabulary.CODE.getIRI());
    }

    public Optional<String> hltQuery(OWLClass owlClass)
    {
        return maybeAnnotationValue(owlClass, OwlVocabulary.HLT_QUERY.getIRI());
    }

    private Optional<String> maybeAnnotationValue(OWLClass owlClass, IRI annotationPropertyIri)
    {
        return EntitySearcher.getAnnotations(
                owlClass,
                this.ontologyManager.ontology(),
                this.ontologyManager.dataFactory().getOWLAnnotationProperty(annotationPropertyIri)
            )
            .findFirst()
            .map(OWLAnnotation::getValue)
            .filter(OWLAnnotationValue::isLiteral)
            .flatMap(OWLAnnotationValue::asLiteral)
            .map(OWLLiteral::getLiteral);
    }
}
