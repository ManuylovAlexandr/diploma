package com.project.port.adapter.semantic.resource;

import static com.project.port.adapter.semantic.Namespaces.PREFIX;
import static java.util.Comparator.comparing;
import static org.springframework.http.ResponseEntity.ok;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import com.project.domain.termquery.TermQueryRepository;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.reasoner.DLQueryResultWrapper;
import com.project.port.adapter.semantic.reasoner.DLQueryService;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "backoffice:SemanticEngineApi")
public class SemanticEngineResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticEngineResource.class);

    private final OntologyManager ontologyManager;

    private final DLQueryService dlQueryService;

    private final TermQueryRepository repository;

    public SemanticEngineResource(
        OntologyManager ontologyManager,
        DLQueryService dlQueryService,
        TermQueryRepository repository
    )
    {
        this.ontologyManager = ontologyManager;
        this.dlQueryService = dlQueryService;
        this.repository = repository;
    }

    @GetMapping(value = "/backoffice/terms")
    public ResponseEntity<List<TermQueryInfo>> getTermsList()
    {
        return ok(this.repository.all().stream()
            .map(termQuery -> new TermQueryInfo()
                .id(termQuery.id().asText())
                .canonicalName(termQuery.canonicalName().asText())
                .query(termQuery.query())
            ).sorted(comparing(TermQueryInfo::getCanonicalName)).toList());
    }

    @GetMapping(value = "/backoffice/queries")
    public ResponseEntity<DLQueryResultWrapper.ReasonerFullResponse> executeDLQuery(
        @RequestParam(value = "query") String query
    )
    {
        LOGGER.info("Ask reasoner for query: {}", query);
        return ok(this.dlQueryService.askQueryForFullResult(query));
    }

    @GetMapping(value = "/backoffice/ontology")
    @ResponseBody
    public ResponseEntity<Resource> getOntology()
        throws OWLOntologyStorageException
    {
        var format = new ManchesterSyntaxDocumentFormat();
        var originalFormat = this.ontologyManager.ontology().getNonnullFormat();
        if (originalFormat.isPrefixOWLDocumentFormat())
        {
            format.copyPrefixesFrom(originalFormat.asPrefixOWLDocumentFormat());
        }
        Optional.ofNullable(this.ontologyManager.defaultPrefixManager().getDefaultPrefix())
            .ifPresent(propPrefix -> format.setPrefix("prop", propPrefix));
        format.setPrefix("av", PREFIX.namespace());

        var buffer = new ByteArrayOutputStream();
        this.ontologyManager.ontology().saveOntology(format, buffer);

        var resource = new ByteArrayResource(buffer.toByteArray());

        return ok()
            .contentType(MediaType.TEXT_PLAIN)
            .contentLength(resource.contentLength())
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment").filename("project.owl").build().toString()
            )
            .body(resource);
    }
}
