package co.empathy.academy.search.controllers;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.services.QueriesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class QueriesController {

    private final QueriesService queriesService;

    public QueriesController(QueriesService queriesService) {
        this.queriesService = queriesService;
    }

    /**
     * GET "/search" endpoint that returns the query and the cluster name
     *
     * @param query - query to search
     * @return QueryResponse with the query and the cluster names
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResponse search(@RequestParam("query") String query) {
        return queriesService.search(query);
    }

    /**
     * GET /search/multi - Performs a multi match query to the movies index
     *
     * @param query  Query to search
     * @param fields Fields to search
     * @return ResponseEntity with the list of movies
     */
    @GetMapping(value = "/multi", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> multiMatch(@RequestParam("query") String query, @RequestParam("fields") String fields) {
        return ResponseEntity.ok().body(queriesService.multiMatch(query, fields));
    }

    /**
     * GET /search/term - Performs a term query to the movies index
     *
     * @param value Value to search
     * @param field Field to search
     * @return ResponseEntity with the list of movies
     */
    @GetMapping(value = "/term", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termQuery(@RequestParam("value") String value, @RequestParam("field") String field) {
        return ResponseEntity.ok().body(queriesService.termQuery(value, field));
    }

    /**
     * GET /search/terms - Performs a terms query to the movies index
     *
     * @param values Values to search
     * @param field  Field to search
     * @return ResponseEntity with the list of movies
     */
    @GetMapping(value = "/terms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termsQuery(@RequestParam("values") String values, @RequestParam("field") String field) {
        return ResponseEntity.ok().body(queriesService.termsQuery(values, field));
    }
}
