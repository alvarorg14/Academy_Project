package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.util.IMDbReader;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class IndexServiceImplTest {

    private final Movie movie1 = new Movie("tconst1", "titleType1", "primaryTitle1",
            "originalTitle1", false, 0, 0, 0, "genres1");
    private final Movie movie2 = new Movie("tconst2", "titleType2", "primaryTitle2",
            "originalTitle2", false, 0, 0, 0, "genres2");
    private final List<Movie> movies = new ArrayList<>() {{
        add(movie1);
        add(movie2);
    }};

    private final ElasticEngine engine = mock(ElasticEngine.class);
    private final IMDbReader reader = mock(IMDbReader.class);

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenMoviesIndexed() {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        IndexServiceImpl service = new IndexServiceImpl(engine);

        assertDoesNotThrow(() -> service.indexImdbData(new MockMultipartFile("file", "file",
                "text/plain", "data".getBytes())));

    }

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenBulkIndexException() throws BulkIndexException, IOException {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        doThrow(BulkIndexException.class).when(engine).indexBulk(any());

        IndexServiceImpl service = new IndexServiceImpl(engine);

        Exception exception = assertThrows(BulkIndexException.class, () -> service.indexImdbData(new MockMultipartFile("file", "file",
                "text/plain", "data".getBytes())));
    }

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenIOException() throws BulkIndexException, IOException {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        doThrow(IOException.class).when(engine).indexBulk(any());

        IndexServiceImpl service = new IndexServiceImpl(engine);

        Exception exception = assertThrows(IOException.class, () -> service.indexImdbData(new MockMultipartFile("file", "file",
                "text/plain", "data".getBytes())));
    }


}