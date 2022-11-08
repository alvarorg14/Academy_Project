package co.empathy.academy.search.controllers;

import co.empathy.academy.search.models.Aka;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.services.QueriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class QueriesControllerTest {

    private final List<Aka> akas = new ArrayList<>() {{
        add(new Aka("title", "region", "language", true));
    }};
    private final Movie movie1 = new Movie("tconst1", "titleType1", "primaryTitle1",
            "originalTitle1", false, 0, 0, 0,
            new String[]{"genres1"}, 5.0, 10, akas);
    private final Movie movie2 = new Movie("tconst2", "titleType2", "primaryTitle2",
            "originalTitle2", false, 0, 0, 0,
            new String[]{"genres2"}, 5.0, 10, akas);

    private final List<Movie> movies = new ArrayList<>() {{
        add(movie1);
        add(movie2);
    }};
    private final QueriesService service = mock(QueriesService.class);
    private final int EXPECTED_SUCCESS_STATUS = 200;
    private final int EXPECTED_ERROR_STATUS = 500;
    private final String query = "query";
    private final String field = "field";

    @Test
    void givenQuery_whenSearch_thenQueryResponse() {
        String query = "Query";
        QueryResponse response = new QueryResponse(query, "clusterName");

        given(service.search(query)).willReturn(response);

        QueriesController queriesController = new QueriesController(service);

        ResponseEntity<QueryResponse> result = queriesController.search(query);

        assertEquals(response, result.getBody());
    }

    @Test
    void givenQueryAndFields_whenMultiMatch_thenQueryResponse() throws IOException {
        String fields = "field1,field2";

        given(service.multiMatch(query, fields)).willReturn(movies);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<List<Movie>> response = controller.multiMatch(query, fields);

        assertEquals(EXPECTED_SUCCESS_STATUS, response.getStatusCodeValue());
        assertEquals(movies, response.getBody());
    }

    @Test
    void givenQueryAndFields_whenMultiMatch_thenInternalServerError() throws IOException {
        String fields = "field1,field2";

        given(service.multiMatch(query, fields)).willThrow(IOException.class);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<List<Movie>> response = controller.multiMatch(query, fields);

        assertEquals(EXPECTED_ERROR_STATUS, response.getStatusCodeValue());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenQueryResponse() throws IOException {
        given(service.termQuery(query, field)).willReturn(movies);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<List<Movie>> response = controller.termQuery(query, field);

        assertEquals(EXPECTED_SUCCESS_STATUS, response.getStatusCodeValue());
        assertEquals(movies, response.getBody());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenInternalServerError() throws IOException {
        given(service.termQuery(query, field)).willThrow(IOException.class);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<List<Movie>> response = controller.termQuery(query, field);

        assertEquals(EXPECTED_ERROR_STATUS, response.getStatusCodeValue());
    }

    @Test
    void givenQueriesAndField_whenTermsQuery_thenQueryResponse() throws IOException {
        given(service.termQuery(query, field)).willReturn(movies);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<List<Movie>> response = controller.termQuery(query, field);

        assertEquals(EXPECTED_SUCCESS_STATUS, response.getStatusCodeValue());
        assertEquals(movies, response.getBody());
    }

    @Test
    void givenQueriesAndField_whenTermsQuery_thenInternalServerError() throws IOException {
        String queries = "query1,query2";
        given(service.termsQuery(queries, field)).willThrow(IOException.class);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<List<Movie>> response = controller.termsQuery(queries, field);

        assertEquals(EXPECTED_ERROR_STATUS, response.getStatusCodeValue());
    }

}