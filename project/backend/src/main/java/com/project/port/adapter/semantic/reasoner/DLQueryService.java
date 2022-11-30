package com.project.port.adapter.semantic.reasoner;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClass;
import org.springframework.stereotype.Service;

@Service
public class DLQueryService
{

    private final DLQueryEngine dlQueryEngine;

    private final DLQueryResultWrapper dlQueryResultWrapper;

    public DLQueryService(DLQueryEngine dlQueryEngine, DLQueryResultWrapper dlQueryResultWrapper)
    {
        this.dlQueryEngine = dlQueryEngine;
        this.dlQueryResultWrapper = dlQueryResultWrapper;
    }

    public DLQueryResultWrapper.ReasonerFullResponse askQueryForFullResult(@Nonnull String query)
    {
        return this.dlQueryResultWrapper.askQuery(query);
    }

    public Stream<OWLClass> askQuery(@Nonnull String query)
    {
        return this.dlQueryEngine.getSubClasses(query, false);
    }
}
