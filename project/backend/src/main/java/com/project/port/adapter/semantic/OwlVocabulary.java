package com.project.port.adapter.semantic;

import static com.project.port.adapter.semantic.Namespaces.PREFIX;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;

public enum OwlVocabulary implements HasIRI
{
    CODE(PREFIX, "Code"),
    SYNONYM(PREFIX, "Synonym"),
    DESCRIPTION(PREFIX, "Description"),
    EQUIVALENCE_CLASS(PREFIX, "EquivalenceClass"),
    CREATED_AT(PREFIX, "CreatedAt"),
    HAS_COMPONENT(PREFIX, "hasComponent"),
    HLT_QUERY(PREFIX, "Query");

    private final IRI iri;

    private final String shortName;

    OwlVocabulary(Namespaces namespace, String shortName)
    {
        this.iri = IRI.create(namespace.namespace(), shortName);
        this.shortName = shortName;
    }

    @Override
    public IRI getIRI()
    {
        return this.iri;
    }

    public String shortName()
    {
        return this.shortName;
    }
}
