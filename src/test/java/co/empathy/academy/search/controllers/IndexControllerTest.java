package co.empathy.academy.search.controllers;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.services.IndexService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    private final Movie movie1 = new Movie("tconst1", "titleType1", "primaryTitle1",
            "originalTitle1", false, 0, 0, 0, "genres1");
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
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "data".getBytes());
        doNothing().when(service).indexImdbData(file);

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.indexImdbData(file);
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    void givenFile_whenIndexImdbData_thenBulkIndexException() throws BulkIndexException, IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "data".getBytes());
        doThrow(BulkIndexException.class).when(service).indexImdbData(file);

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.indexImdbData(file);
        assertEquals(EXPECTED_BAD_REQUEST_CODE, response.getStatusCodeValue());
    }

    @Test
    void givenFile_whenIndexImdbData_thenIOException() throws BulkIndexException, IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "data".getBytes());
        doThrow(IOException.class).when(service).indexImdbData(file);

        IndexController controller = new IndexController(service);
        ResponseEntity<String> response = controller.indexImdbData(file);
        assertEquals(EXPECTED_ERROR_CODE, response.getStatusCodeValue());
    }

}