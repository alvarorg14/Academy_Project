package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.BulkIndexException;
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
     * @param basicsFile     File with the imdb basics data
     * @param ratingsFile    File with the imdb ratings data
     * @param akasFile       File with the imdb akas data
     * @param crewFile       File with the imdb crew data
     * @param principalsFile File with the imdb principals data
     */
    void indexImdbData(MultipartFile basicsFile, MultipartFile ratingsFile,
                       MultipartFile akasFile, MultipartFile crewFile, MultipartFile principalsFile)
            throws IOException, BulkIndexException;

    /**
     * Indexes names data
     *
     * @param namesFile File with the names data
     */
    void indexNamesData(MultipartFile namesFile) throws IOException, BulkIndexException;
}
