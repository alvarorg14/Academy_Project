package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.models.facets.Facet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {

    /*
    void makeAggsQuery() throws IOException;*/

    QueryResponse search(String query);

    /**
     * Performs a multi match query to the movies index
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return List of movies
     */
    List<Movie> multiMatch(String query, String fields) throws IOException;

    /**
     * Performs a term query to the movies index
     *
     * @param value - value to search
     * @param field - field to search
     * @return List of movies
     */
    List<Movie> termQuery(String value, String field) throws IOException;

    /**
     * Performs a terms query to the movies index
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of movies
     */
    List<Movie> termsQuery(String values, String field) throws IOException;

    /**
     * Performs a search with all the filters
     *
     * @param genres     Genres to search
     * @param types      Types to search
     * @param maxYear    Maximum year to search
     * @param minYear    Minimum year to search
     * @param maxRuntime Maximum runtime minutes
     * @param minRuntime Minimum runtime minutes
     * @param maxScore   Maximum average rating
     * @param minScore   Minimum average rating
     * @param maxNHits   Maximum number of hits
     * @return List of movies that match the filters
     * @throws IOException
     */
    List<Movie> allFiltersSearch(Optional<String> genres, Optional<String> types,
                                 Optional<Integer> maxYear, Optional<Integer> minYear,
                                 Optional<Integer> maxRuntime, Optional<Integer> minRuntime,
                                 Optional<Double> maxScore, Optional<Double> minScore,
                                 Optional<Integer> maxNHits) throws IOException;

    /**
     * Performs a search to get the genres aggregation
     *
     * @return Facet with the genres aggregation
     * @throws IOException
     */
    Facet getGenresAggregation() throws IOException;
}
