package com.project.port.adapter.semantic.reasoner;

import static com.project.common.CollectionUtils.mapToSet;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.stereotype.Service;

import com.project.port.adapter.semantic.OntologyManager;

@Service
public class DLQueryResultWrapper
{

    private final DLQueryEngine dlQueryEngine;

    private final OntologyManager ontologyManager;

    /**
     * @param engine the engine
     */
    public DLQueryResultWrapper(OntologyManager ontologyManager, DLQueryEngine engine)
    {
        this.ontologyManager = ontologyManager;
        this.dlQueryEngine = engine;
    }

    /**
     * @param classExpression the class expression to use for interrogation
     */
    public ReasonerFullResponse askQuery(String classExpression)
    {
        if (classExpression.isEmpty())
        {
            return new ReasonerFullResponse(classExpression, new ReasonerFullResult(null, null, null, null));
        } else
        {
            return new ReasonerFullResponse(classExpression, new ReasonerFullResult(
                owlClassesToNames(this.dlQueryEngine.getSuperClasses(classExpression, true).collect(toSet())),
                owlClassesToNames(this.dlQueryEngine.getEquivalentClasses(classExpression).collect(toSet())),
                owlClassesToNames(this.dlQueryEngine.getSubClasses(classExpression, true).collect(toSet())),
                owlClassesToNames(this.dlQueryEngine.getInstances(classExpression, true).collect(toSet()))
            ));
        }
    }

    private Set<String> owlClassesToNames(Set<? extends OWLEntity> owlClasses)
    {
        return mapToSet(owlClasses, this.ontologyManager.shortFormProvider()::getShortForm);
    }

    public record ReasonerFullResponse(
        String query,
        ReasonerFullResult result
    ) {}

    public record ReasonerFullResult(
        Set<String> superClasses,
        Set<String> equivalentClasses,
        Set<String> subClasses,
        Set<String> instances
    ) {}
}
