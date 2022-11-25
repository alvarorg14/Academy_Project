package co.empathy.academy.search.controllers;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.models.SearchResponse;
import co.empathy.academy.search.models.facets.Facet;
import co.empathy.academy.search.models.facets.FacetValue;
import co.empathy.academy.search.services.SearchService;
import co.empathy.academy.search.util.ResourcesUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueriesControllerTest {

    private final List<Movie> movies = ResourcesUtil.getMovies();

    private final SearchService service = mock(SearchService.class);
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

    @Test
    void givenAllFilters_whenAllFiltersSearch_thenResponseReturned() throws IOException {
        given(service.allFiltersSearch(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).willReturn(movies);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<SearchResponse> response = controller.allFiltersSearch(Optional.of("genres"),
                Optional.of("type"), Optional.of(2017), Optional.of(2018), Optional.of(1),
                Optional.of(10), Optional.of(5.0), Optional.of(10.0), Optional.of(100), Optional.of("asc"));

        assertEquals(EXPECTED_SUCCESS_STATUS, response.getStatusCodeValue());
        assertEquals(movies, response.getBody().getHits());
        assertEquals(0, response.getBody().getFacets().size());

        verify(service, times(1)).allFiltersSearch(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void givenAllFilters_whenAllFiltersSearch_thenIOException() throws IOException {
        given(service.allFiltersSearch(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).willThrow(IOException.class);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<SearchResponse> response = controller.allFiltersSearch(Optional.of("genres"),
                Optional.of("type"), Optional.of(2017), Optional.of(2018), Optional.of(1),
                Optional.of(10), Optional.of(5.0), Optional.of(10.0), Optional.of(100), Optional.of("asc"));

        assertEquals(EXPECTED_ERROR_STATUS, response.getStatusCodeValue());
    }

    @Test
    void givenAllUp_whenGenresSearch_thenFacetReturned() throws IOException {
        Facet facet = new Facet("genres", "value",
                List.of(new FacetValue("genre1", "genre1", 1L, "genre1")));

        given(service.getGenresAggregation()).willReturn(facet);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<SearchResponse> response = controller.genresSearch();

        assertEquals(EXPECTED_SUCCESS_STATUS, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getFacets().size());
        assertEquals(facet, response.getBody().getFacets().get(0));
        assertEquals(0, response.getBody().getHits().size());

        verify(service, times(1)).getGenresAggregation();
    }

    @Test
    void givenElasticDown_whenGenresSearch_thenInternalServerError() throws IOException {
        given(service.getGenresAggregation()).willThrow(IOException.class);

        QueriesController controller = new QueriesController(service);

        ResponseEntity<SearchResponse> response = controller.genresSearch();

        assertEquals(EXPECTED_ERROR_STATUS, response.getStatusCodeValue());
    }

}