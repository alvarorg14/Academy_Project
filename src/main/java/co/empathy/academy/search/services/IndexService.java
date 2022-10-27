package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;

import java.io.IOException;

public interface IndexService {

    /**
     * Creates an index
     *
     * @param name - name of the index
     * @throws Exception - if the index cannot be created
     */
    void createIndex(String name) throws IOException;

    /**
     * Indexes a document
     *
     * @param indexName - name of the index where the document will be indexed
     * @param movie     - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    void indexDocument(String indexName, Movie movie) throws IOException;
}
