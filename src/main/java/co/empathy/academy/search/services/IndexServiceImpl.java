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
     * @throws IOException - if the index cannot be created
     */
    @Override
    public void createIndex() throws IOException {
        elasticEngine.createIndex();
        elasticEngine.putSettings();
        elasticEngine.putMapping();
    }

    /**
     * Indexes a document
     *
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    @Override
    public void indexDocument(Movie movie) throws IOException {
        elasticEngine.indexDocument(movie);
    }
}
