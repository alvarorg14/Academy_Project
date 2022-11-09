package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.util.IMDbReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    /**
     * Indexes imdb data from a file
     *
     * @param basicsFile  File with the imdb basics data
     * @param ratingsFile File with the imdb ratings data
     * @param akasFile    File with the imdb akas data
     * @param crewFile    File with the imdb crew data
     * @return True if the data was indexed correctly, false otherwise
     */
    @Override
    public void indexImdbData(MultipartFile basicsFile, MultipartFile ratingsFile,
                              MultipartFile akasFile, MultipartFile crewFile) throws IOException, BulkIndexException {
        IMDbReader reader = new IMDbReader(basicsFile, ratingsFile, akasFile, crewFile);

        while (reader.hasDocuments()) {
            List<Movie> movies = reader.readDocuments();
            elasticEngine.indexBulk(movies);
        }
    }
}
