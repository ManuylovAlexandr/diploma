package com.project.port.adapter.semantic.reasoner;

import java.util.Optional;

import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxParserImpl;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OntologyConfigurator;
import org.springframework.stereotype.Component;

import com.project.port.adapter.semantic.OntologyManager;

@Component
public class DLQueryParser
{

    private final OntologyManager ontologyManager;

    /**
     * Constructs a DLQueryParser using the specified ontology and short form provider to map entity
     * IRIs to short names.
     */
    DLQueryParser(OntologyManager ontologyManager)
    {
        this.ontologyManager = ontologyManager;
    }

    /**
     * Parses a class expression string to obtain a class expression.
     *
     * @param classExpressionString The class expression string
     *
     * @return The corresponding class expression if the class expression string is malformed or
     * contains unknown entity names.
     */
    public OWLClassExpression parseClassExpression(String classExpressionString)
    {
        var parser = new ManchesterOWLSyntaxParserImpl(new OntologyConfigurator(), this.ontologyManager.dataFactory());

        parser.setOWLEntityChecker(new ProtegeOwlEntityChecker(
            this.ontologyManager.bidirectionalShortFormProviderAdapter()
        ));
        return parser.parseClassExpression(classExpressionString);
    }

    public Optional<OWLClassExpression> maybeParseClassExpression(String classExpressionString)
    {
        try
        {
            return Optional.ofNullable(parseClassExpression(classExpressionString));
        }
        catch (OWLParserException ex)
        {
            return Optional.empty();
        }
    }
}
