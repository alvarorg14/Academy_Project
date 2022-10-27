package co.empathy.academy.search.repositories;

import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.util.List;

public interface ElasticEngine {

    /**
     * Creates an index using the elastic client
     *
     * @throws IOException - if the index cannot be created
     */
    void createIndex() throws IOException;

    /**
     * Puts the settings of the index
     *
     * @throws IOException - If the settings cannot be loaded
     */
    void putSettings() throws IOException;

    /**
     * Puts the mapping of the index
     *
     * @throws IOException If the mapping cannot be loaded
     */
    void putMapping() throws IOException;

    /**
     * Indexes a movie document
     *
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    void indexDocument(Movie movie) throws IOException;

    /**
     * Performs a multi match query
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return - list of movies that match the query
     */
    List<Movie> multiMatch(String query, String[] fields);

    /**
     * Performs a term query
     *
     * @param field - field to search
     * @param value - value to search
     * @return - list of movies that match the query
     */
    List<Movie> termQuery(String value, String field);

    /**
     * Performs a terms query
     *
     * @param values Values to search
     * @param field  Field to search
     * @return List of movies that match the query
     */
    List<Movie> termsQuery(String[] values, String field);
}
