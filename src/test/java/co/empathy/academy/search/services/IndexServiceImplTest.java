package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.names.ElasticNamesEngine;
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
import static org.mockito.Mockito.*;

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
    private final ElasticNamesEngine namesEngine = mock(ElasticNamesEngine.class);
    private final IMDbReader reader = mock(IMDbReader.class);

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenMoviesIndexed() {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);

        assertDoesNotThrow(() -> service.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile));

    }

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenBulkIndexException() throws BulkIndexException, IOException {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        doThrow(BulkIndexException.class).when(engine).indexBulk(any());

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);

        Exception exception = assertThrows(BulkIndexException.class,
                () -> service.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile));
    }

    @Test
    void givenAFileWithMovies_whenIndexIMDbData_thenIOException() throws BulkIndexException, IOException {
        given(reader.hasDocuments()).willReturn(true);
        given(reader.readDocuments()).willReturn(movies);

        doThrow(IOException.class).when(engine).indexBulk(any());

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);

        Exception exception = assertThrows(IOException.class,
                () -> service.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile));
    }

    @Test
    void givenElasticUp_whenCreateIndex_thenIndexCreated() throws IOException {
        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);
        assertDoesNotThrow(() -> service.createIndex());

        verify(engine, times(1)).createIndex();
        verify(engine, times(1)).putSettings();
        verify(engine, times(1)).putMapping();
    }

    @Test
    void givenElasticDown_whenCreateIndex_thenErrorCreating() throws IOException {
        doThrow(IOException.class).when(engine).createIndex();

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);
        assertThrows(IOException.class, () -> service.createIndex());

        verify(engine, times(1)).createIndex();
        verify(engine, times(0)).putSettings();
        verify(engine, times(0)).putMapping();
    }

    @Test
    void givenElasticDown_whenCreateIndex_thenErrorSetting() throws IOException {
        doThrow(IOException.class).when(engine).putSettings();

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);
        assertThrows(IOException.class, () -> service.createIndex());

        verify(engine, times(1)).createIndex();
        verify(engine, times(1)).putSettings();
        verify(engine, times(0)).putMapping();
    }

    @Test
    void givenElasticDown_whenCreateIndex_thenErrorMapping() throws IOException {
        doThrow(IOException.class).when(engine).putMapping();

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);
        assertThrows(IOException.class, () -> service.createIndex());

        verify(engine, times(1)).createIndex();
        verify(engine, times(1)).putSettings();
        verify(engine, times(1)).putMapping();
    }

    @Test
    void givenElasticUpAndMovie_whenIndexDocument_thenDocumentIndexed() throws IOException {
        doNothing().when(engine).indexDocument(any());

        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);
        assertDoesNotThrow(() -> service.indexDocument(movies.get(0)));

        verify(engine, times(1)).indexDocument(any());
    }

    @Test
    void givenElasticDownAndMovie_whenIndexDocument_thenErrorIndexing() throws IOException {
        doThrow(IOException.class).when(engine).indexDocument(any());
        IndexServiceImpl service = new IndexServiceImpl(engine, namesEngine);
        assertThrows(IOException.class, () -> service.indexDocument(movies.get(0)));

        verify(engine, times(1)).indexDocument(any());
    }


}