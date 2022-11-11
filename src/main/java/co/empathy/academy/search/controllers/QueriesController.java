package co.empathy.academy.search.controllers;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search")
public class QueriesController {

    private final SearchService searchService;

    public QueriesController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * GET "/search" endpoint that returns the query and the cluster name
     *
     * @param query - query to search
     * @return QueryResponse with the query and the cluster names
     */
    @Operation(summary = "Return elastic search information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
    })
    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResponse> search(@RequestParam("query") String query) {
        return ResponseEntity.ok(searchService.search(query));
    }

    /**
     * GET /search/multi - Performs a multi match query to the movies index
     *
     * @param query  Query to search
     * @param fields Fields to search
     * @return ResponseEntity with the list of movies
     */
    @Operation(summary = "Get movies by a multi match query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the movies")
    })
    @GetMapping(value = "/multi", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> multiMatch(@RequestParam("query") String query, @RequestParam("fields") String fields) {
        try {
            return ResponseEntity.ok(searchService.multiMatch(query, fields));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /search/term - Performs a term query to the movies index
     *
     * @param value Value to search
     * @param field Field to search
     * @return ResponseEntity with the list of movies
     */
    @Operation(summary = "Get movies by a term query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the movies")
    })
    @GetMapping(value = "/term", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termQuery(@RequestParam("value") String value, @RequestParam("field") String field) {
        try {
            return ResponseEntity.ok().body(searchService.termQuery(value, field));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /search/terms - Performs a terms query to the movies index
     *
     * @param values Values to search
     * @param field  Field to search
     * @return ResponseEntity with the list of movies
     */
    @Operation(summary = "Get movies by a terms query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the movies")
    })
    @GetMapping(value = "/terms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termsQuery(@RequestParam("values") String values, @RequestParam("field") String field) {
        try {
            return ResponseEntity.ok().body(searchService.termsQuery(values, field));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    /**
     * GET /aggs

    @GetMapping(value = "/aggs", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aggs() throws IOException {
        searchService.makeAggsQuery();
    }*/

    /**
     * GET /search - Performs a search with all the filters
     *
     * @param genres     Genres to search
     * @param types      Types to search
     * @param maxYear    Maximum year to search
     * @param minYear    Minimum year to search
     * @param maxMinutes Maximum runtime minutes
     * @param minMinutes Minimum runtime minutes
     * @param maxScore   Maximum average rating
     * @param minScore   Minimum average rating
     * @return List of movies that match the filters
     */
    @Operation(summary = "Get movies by a basic filters query")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> allFiltersSearch(@RequestParam("genres") Optional<String> genres,
                                                        @RequestParam("types") Optional<String> types,
                                                        @RequestParam("maxYear") Optional<Integer> maxYear,
                                                        @RequestParam("minYear") Optional<Integer> minYear,
                                                        @RequestParam("maxMinutes") Optional<Integer> maxMinutes,
                                                        @RequestParam("minMinutes") Optional<Integer> minMinutes,
                                                        @RequestParam("maxScore") Optional<Double> maxScore,
                                                        @RequestParam("minScore") Optional<Double> minScore) {
        try {
            List<Movie> movies = searchService.allFiltersSearch(genres, types, maxYear, minYear,
                    maxMinutes, minMinutes, maxScore, minScore);
            return ResponseEntity.ok(movies);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
