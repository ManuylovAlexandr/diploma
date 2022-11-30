package com.project.port.adapter.semantic.reasoner;

import static java.util.function.Predicate.not;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.springframework.stereotype.Service;

import com.project.port.adapter.semantic.OntologyManager;

@Service
public class DLQueryEngine
{

    private final Predicate<OWLClass> superClassesFilter = cls -> !cls.isOWLThing();

    private final Predicate<OWLClass> subClassesFilter = cls -> !cls.isOWLNothing();

    private final OntologyManager ontologyManager;

    private final DLQueryParser parser;

    /**
     * Constructs a DLQueryEngine. This will answer "DL queries" using the specified reasoner. A
     * short form provider specifies how entities are rendered.
     */
    public DLQueryEngine(OntologyManager ontologyManager, DLQueryParser dlQueryParser)
    {
        this.ontologyManager = ontologyManager;
        this.parser = dlQueryParser;
    }

    /**
     * Gets the superclasses of a class expression parsed from a string.
     *
     * @param classExpressionString The string from which the class expression will be parsed.
     * @param direct Specifies whether direct superclasses should be returned or not.
     *
     * @return The superclasses of the specified class expression If there was a problem parsing the
     * class expression.
     */
    @Nonnull
    public Stream<OWLClass> getSuperClasses(@Nullable String classExpressionString, boolean direct)
    {
        return queryInferenceStream(
            classExpressionString,
            classExpression -> this.ontologyManager.reasoner()
                .superClasses(classExpression, direct)
                .filter(this.superClassesFilter)
        );
    }

    /**
     * Gets the equivalent classes of a class expression parsed from a string.
     *
     * @param classExpressionString The string from which the class expression will be parsed.
     *
     * @return The equivalent classes of the specified class expression If there was a problem
     * parsing the class expression.
     */
    @Nonnull
    public Stream<OWLClass> getEquivalentClasses(@Nullable String classExpressionString)
    {
        return queryInferenceStream(
            classExpressionString,
            classExpression -> this.ontologyManager.reasoner().equivalentClasses(classExpression)
        );
    }

    /**
     * Gets the subclasses of a class expression parsed from a string.
     *
     * @param classExpressionString The string from which the class expression will be parsed.
     * @param direct Specifies whether direct subclasses should be returned or not.
     *
     * @return The subclasses of the specified class expression If there was a problem parsing the
     * class expression.
     */
    @Nonnull
    public Stream<OWLClass> getSubClasses(@Nullable String classExpressionString, boolean direct)
    {
        return queryInferenceStream(
            classExpressionString,
            classExpression -> this.ontologyManager.reasoner()
                .subClasses(classExpression, direct)
                .filter(this.subClassesFilter)
        );
    }

    /**
     * Gets the instances of a class expression parsed from a string.
     *
     * @param classExpressionString The string from which the class expression will be parsed.
     * @param direct Specifies whether direct instances should be returned or not.
     *
     * @return The instances of the specified class expression If there was a problem parsing the
     * class expression.
     */
    @Nonnull
    public Stream<OWLNamedIndividual> getInstances(@Nullable String classExpressionString, boolean direct)
    {
        return queryInferenceStream(
            classExpressionString,
            classExpression -> this.ontologyManager.reasoner().instances(classExpression, direct)
        );
    }

    @Nonnull
    private <E extends OWLObject> Stream<E> queryInferenceStream(
        @Nullable String classExpressionString,
        @Nonnull Function<OWLClassExpression, Stream<E>> reasonerMapping
    )
    {
        return Optional.ofNullable(classExpressionString)
            .map(String::trim)
            .filter(not(String::isEmpty))
            .flatMap(this::parseClassExpressionSafe)
            .map(reasonerMapping)
            .orElse(Stream.empty());
    }

    private Optional<OWLClassExpression> parseClassExpressionSafe(@Nonnull String classExpressionString)
    {
        try
        {
            return Optional.ofNullable(this.parser.parseClassExpression(classExpressionString));
        }
        catch (OWLParserException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
}
