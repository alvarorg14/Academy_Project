package co.empathy.academy.search.controllers;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Aka;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.services.IndexService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    private final List<Aka> akas = new ArrayList<>() {{
        add(new Aka("title", "region", "language", true));
    }};
    private final Movie movie1 = new Movie("tconst1", "titleType1", "primaryTitle1",
            "originalTitle1", false, 0, 0, 0, new String[]{"genres1"},
            5.0, 10, akas);

    private final MultipartFile basicsFile = new MockMultipartFile("basics", "basics.txt",
            "text/plain", "basics".getBytes());
    private final MultipartFile ratingsFile = new MockMultipartFile("ratings", "ratings.txt",
            "text/plain", "rating".getBytes());

    private final MultipartFile akasFile = new MockMultipartFile("akas", "akas.txt",
            "text/plain", "akas".getBytes());
    private final int EXPECTED_SUCCESS_CODE = 200;
    private final int EXPECTED_BAD_REQUEST_CODE = 400;
    private final int EXPECTED_ERROR_CODE = 500;

    IndexService service = mock(IndexService.class);

    @Test
    void givenElasticUp_whenCreateIndex_thenIndexCreated() throws IOException {
        doNothing().when(service).createIndex();

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.createIndex();
        assertEquals(EXPECTED_SUCCESS_CODE, response.getStatusCodeValue());
        assertEquals("Index created", response.getBody());
    }

    @Test
    void givenElasticDown_whenCreateIndex_thenIOException() throws IOException {
        doThrow(IOException.class).when(service).createIndex();

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.createIndex();
        assertEquals(EXPECTED_ERROR_CODE, response.getStatusCodeValue());
        assertEquals("Error creating index", response.getBody());
    }

    @Test
    void givenMovie_whenIndexDocument_thenDocumentIndexed() throws IOException {
        doNothing().when(service).indexDocument(any());

        IndexController controller = new IndexController(service);
        ResponseEntity<Movie> response = controller.indexDocument(movie1);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(movie1, response.getBody());
    }

    @Test
    void givenMovie_whenIndexDocument_thenIOException() throws IOException {
        doThrow(IOException.class).when(service).indexDocument(any());

        IndexController controller = new IndexController(service);
        ResponseEntity<Movie> response = controller.indexDocument(movie1);
        assertEquals(EXPECTED_ERROR_CODE, response.getStatusCodeValue());
    }

    @Test
    void givenFile_whenIndexImdbData_thenDataIndexed() throws BulkIndexException, IOException {
        doNothing().when(service).indexImdbData(basicsFile, ratingsFile, akasFile);

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.indexImdbData(basicsFile, ratingsFile, akasFile);
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    void givenFile_whenIndexImdbData_thenBulkIndexException() throws BulkIndexException, IOException {
        doThrow(BulkIndexException.class).when(service).indexImdbData(basicsFile, ratingsFile, akasFile);

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.indexImdbData(basicsFile, ratingsFile, akasFile);
        assertEquals(EXPECTED_BAD_REQUEST_CODE, response.getStatusCodeValue());
    }

    @Test
    void givenFile_whenIndexImdbData_thenIOException() throws BulkIndexException, IOException {
        doThrow(IOException.class).when(service).indexImdbData(basicsFile, ratingsFile, akasFile);

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.indexImdbData(basicsFile, ratingsFile, akasFile);
        assertEquals(EXPECTED_ERROR_CODE, response.getStatusCodeValue());
    }

}