package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticEngine;

import java.io.IOException;

public class IndexServiceImpl implements IndexService {


    private final ElasticEngine elasticEngine;

    public IndexServiceImpl(ElasticEngine elasticEngine) {
        this.elasticEngine = elasticEngine;
    }

    /**
     * Creates an index in elastic
     *
     * @param name - name of the index
     * @throws Exception - if the index cannot be created
     */
    @Override
    public void createIndex(String name) throws Exception {
        elasticEngine.createIndex(name);
    }

    /**
     * Indexes a document
     *
     * @param indexName - name of the index where the document will be indexed
     * @param movie     - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    @Override
    public void indexDocument(String indexName, Movie movie) throws IOException {
        elasticEngine.indexDocument(indexName, movie);
    }
}
