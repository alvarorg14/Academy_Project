package co.empathy.academy.search.services;

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
     * @param file
     * @return True if the data was indexed correctly, false otherwise
     */
    @Override
    public boolean indexImdbData(MultipartFile file) {
        IMDbReader reader = new IMDbReader(file);

        while (reader.hasDocuments()) {
            List<Movie> movies = reader.readDocuments();
            boolean result = elasticEngine.indexBulk(movies);
            if (!result) {
                return false;
            }
        }
        return true;
    }
}
