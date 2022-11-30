package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.Name;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.names.ElasticNamesEngine;
import co.empathy.academy.search.util.IMDbReader;
import co.empathy.academy.search.util.NamesReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class IndexServiceImpl implements IndexService {


    private final ElasticEngine elasticEngine;
    private final ElasticNamesEngine elasticNamesEngine;

    public IndexServiceImpl(ElasticEngine elasticEngine, ElasticNamesEngine elasticNamesEngine) {
        this.elasticEngine = elasticEngine;
        this.elasticNamesEngine = elasticNamesEngine;
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
     * @param basicsFile     File with the imdb basics data
     * @param ratingsFile    File with the imdb ratings data
     * @param akasFile       File with the imdb akas data
     * @param crewFile       File with the imdb crew data
     * @param principalsFile File with the imdb principals data
     * @throws IOException        - if the data cannot be indexed
     * @throws BulkIndexException - if there was an error in the bulk petition
     */
    @Override
    public void indexImdbData(MultipartFile basicsFile, MultipartFile ratingsFile,
                              MultipartFile akasFile, MultipartFile crewFile, MultipartFile principalsFile)
            throws IOException, BulkIndexException {
        IMDbReader reader = new IMDbReader(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);

        while (reader.hasDocuments()) {
            List<Movie> movies = reader.readDocuments();
            elasticEngine.indexBulk(movies);
        }
    }

    /**
     * Indexes names data
     *
     * @param namesFile File with the names data
     */
    @Override
    public void indexNamesData(MultipartFile namesFile) throws IOException, BulkIndexException {
        elasticNamesEngine.createIndex();
        elasticNamesEngine.putMapping();

        NamesReader reader = new NamesReader(namesFile);

        while (reader.hasDocuments()) {
            List<Name> names = reader.readDocuments();
            elasticNamesEngine.indexBulk(names);
        }
    }

}
