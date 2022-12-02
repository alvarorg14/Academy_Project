package co.empathy.academy.search.controllers;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.Name;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.models.SearchResponse;
import co.empathy.academy.search.models.facets.Facet;
import co.empathy.academy.search.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
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
     * GET /search - Performs a search with all the filters
     *
     * @param genres     Genres to search
     * @param type       Types to search
     * @param maxYear    Maximum year to search
     * @param minYear    Minimum year to search
     * @param maxMinutes Maximum runtime minutes
     * @param minMinutes Minimum runtime minutes
     * @param maxScore   Maximum average rating
     * @param minScore   Minimum average rating
     * @param maxNHits   Maximum number of hits
     * @param sortRating Sort by rating
     * @return List of movies that match the filters
     */
    @Operation(summary = "Get movies by a basic filters query")
    @Parameter(name = "genres", description = "Genres of the movie. Can be multiple ones, separated by commas. It matches exactly")
    @Parameter(name = "type", description = "Type of the movie. Can be multiple ones, separated by commas. It matches exactly")
    @Parameter(name = "maxYear", description = "Maximum year of the movie")
    @Parameter(name = "minYear", description = "Minimum year of the movie")
    @Parameter(name = "maxMinutes", description = "Maximum runtime minutes of the movie")
    @Parameter(name = "minMinutes", description = "Minimum runtime minutes of the movie")
    @Parameter(name = "maxScore", description = "Maximum average rating of the movie")
    @Parameter(name = "minScore", description = "Minimum average rating of the movie")
    @Parameter(name = "maxNHits", description = "Maximum number of hits to return")
    @Parameter(name = "sortRating", description = "Sort by average rating. Ascendant or descendant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the movies")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResponse> allFiltersSearch(@RequestParam("genres") Optional<String> genres,
                                                           @RequestParam("type") Optional<String> type,
                                                           @RequestParam("maxYear") Optional<Integer> maxYear,
                                                           @RequestParam("minYear") Optional<Integer> minYear,
                                                           @RequestParam("maxMinutes") Optional<Integer> maxMinutes,
                                                           @RequestParam("minMinutes") Optional<Integer> minMinutes,
                                                           @RequestParam("maxScore") Optional<Double> maxScore,
                                                           @RequestParam("minScore") Optional<Double> minScore,
                                                           @RequestParam("maxNHits") Optional<Integer> maxNHits,
                                                           @RequestParam("sortRating") Optional<String> sortRating) {
        try {
            List<Movie> movies = searchService.allFiltersSearch(genres, type, maxYear, minYear,
                    maxMinutes, minMinutes, maxScore, minScore, maxNHits, sortRating);
            return ResponseEntity.ok(new SearchResponse(movies, new ArrayList<>()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /search/genres - Returns an aggregation of the genres
     *
     * @return ResponseEntity - 200 with the aggregation of the genres or 500 if there is an error
     */
    @Operation(summary = "Get genres aggregation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres found"),
            @ApiResponse(responseCode = "500", description = "Error searching the genres")
    })
    @GetMapping(value = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResponse> genresSearch() {
        try {
            List<Facet> facets = new ArrayList<>() {{
                add(searchService.getGenresAggregation());
            }};
            return ResponseEntity.ok(new SearchResponse(new ArrayList<>(), facets));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /search/names/{nconst} - Returns the information of a person/name
     *
     * @param nconsts nconst of the person
     * @return ResponseEntity - 200 with the information of the person or 500 if there is an error
     */
    @GetMapping(value = "/names", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Name>> nameSearch(@RequestParam("ids") String nconsts) {
        try {
            return ResponseEntity.ok(searchService.namesSearch(nconsts));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
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
}
