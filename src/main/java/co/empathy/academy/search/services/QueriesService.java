package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;

import java.util.List;

public interface QueriesService {

    QueryResponse search(String query);

    /**
     * Performs a multi match query to the movies index
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return List of movies
     */
    List<Movie> multiMatch(String query, String fields);

    /**
     * Performs a term query to the movies index
     *
     * @param value - value to search
     * @param field - field to search
     * @return List of movies
     */
    List<Movie> termQuery(String value, String field);

    /**
     * Performs a terms query to the movies index
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of movies
     */
    List<Movie> termsQuery(String values, String field);
}
