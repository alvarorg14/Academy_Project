package co.empathy.academy.search.repositories;

import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.util.List;

public interface ElasticEngine {

    /**
     * Creates an index using the elastic client
     *
     * @param name - name of the index
     * @throws Exception - if the index cannot be created
     */
    void createIndex(String name) throws Exception;

    /**
     * Indexes a movie document
     *
     * @param indexName - name of the index where the document will be indexed
     * @param movie     - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    void indexDocument(String indexName, Movie movie) throws IOException;

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
