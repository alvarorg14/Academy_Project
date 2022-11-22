package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.facets.Facet;

import java.io.IOException;
import java.util.List;

public interface ElasticEngine {

    //void makeAggsQuery() throws IOException;

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
     * Makes a query to elasticsearch
     *
     * @param query       Query to make
     * @param maxNHits    Maximum number of hits to return
     * @param sortOptions Sort options
     * @return List of movies that match the query
     */
    List<Movie> performQuery(Query query, Integer maxNHits, List<SortOptions> sortOptions) throws IOException;

    /**
     * Indexes a list of movies
     *
     * @param movies Movies to index
     * @return True if the movies were indexed, false otherwise
     */
    void indexBulk(List<Movie> movies) throws IOException, BulkIndexException;

    /**
     * Gets a genres aggregation
     *
     * @return Facet with the genres aggregation
     */
    Facet getGenresAggregation() throws IOException;
}
