package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IndexService {

    /**
     * Creates an index
     *
     * @throws IOException - if the index cannot be created
     */
    void createIndex() throws IOException;

    /**
     * Indexes a document
     *
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    void indexDocument(Movie movie) throws IOException;

    /**
     * Indexes imdb data from a file
     *
     * @param file
     */
    boolean indexImdbData(MultipartFile file);
}
