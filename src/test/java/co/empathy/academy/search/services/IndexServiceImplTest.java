package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.util.IMDbReader;
import co.empathy.academy.search.util.ResourcesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class IndexServiceImplTest {

    private final List<Movie> movies = ResourcesUtil.getMovies();
    private final MultipartFile basicsFile = new MockMultipartFile("basics", "basics.txt",
            "text/plain", "basics".getBytes());
    private final MultipartFile ratingsFile = new MockMultipartFile("ratings", "ratings.txt",
            "text/plain", "rating".getBytes());
    private final MultipartFile akasFile = new MockMultipartFile("akas", "akas.txt",
            "text/plain", "akas".getBytes());

    private final MultipartFile crewFile = new MockMultipartFile("crew", "crew.txt",
            "text/plain", "crew".getBytes());
    private final MultipartFile principalsFile = new MockMultipartFile("principals", "principals.txt",
            "text/plain", "principals".getBytes());
    private final ElasticEngine engine = mock(ElasticEngine.class);
    private final IMDbReader reader = mock(IMDbReader.class);

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenMoviesIndexed() {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        IndexServiceImpl service = new IndexServiceImpl(engine);

        assertDoesNotThrow(() -> service.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile));

    }

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenBulkIndexException() throws BulkIndexException, IOException {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        doThrow(BulkIndexException.class).when(engine).indexBulk(any());

        IndexServiceImpl service = new IndexServiceImpl(engine);

        Exception exception = assertThrows(BulkIndexException.class,
                () -> service.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile));
    }

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenIOException() throws BulkIndexException, IOException {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        doThrow(IOException.class).when(engine).indexBulk(any());

        IndexServiceImpl service = new IndexServiceImpl(engine);

        Exception exception = assertThrows(IOException.class,
                () -> service.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile));
    }


}