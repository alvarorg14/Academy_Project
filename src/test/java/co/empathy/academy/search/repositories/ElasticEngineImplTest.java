package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.util.ResourcesUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ElasticEngineImplTest {

    private final List<Movie> movies = ResourcesUtil.getMovies();
    ElasticsearchClient client = mock(ElasticsearchClient.class);

    @Test
    void givenQueryAndFields_whenMultiMatchQuery_thenQueryFormed() {
        String query = "query";
        String[] fields = {"field1", "field2"};

        String expectedQuery = "Query: {\"multi_match\":{\"fields\":[\"field1\",\"field2\"],\"query\":\"query\"}}";

        Query multiMatch = new ElasticEngineImpl(client).multiMatch(query, fields);

        assertTrue(multiMatch.isMultiMatch());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenQueryFormed() {
        String query = "query";
        String field = "field";

        String expectedQuery = "Query: {\"term\":{\"field\":{\"value\":\"query\"}}}";

        Query termQuery = new ElasticEngineImpl(client).termQuery(query, field);

        assertTrue(termQuery.isTerm());
    }

    @Test
    void givenQueriesAndField_whenTermsQuery_thenQueryFormed() {
        String[] queries = {"query1", "query2"};
        String field = "field";

        String expectedQuery = "Query: {\"terms\":{\"field\":[\"query1\",\"query2\"]}}";

        Query termsQuery = new ElasticEngineImpl(client).termsQuery(queries, field);

        assertTrue(termsQuery.isTerms());
    }


    @Test
    void givenMovies_whenBulkIndex_thenMoviesIndexed() throws IOException {
        BulkResponse mock = mock(BulkResponse.class);
        given(mock.errors()).willReturn(false);
        given(client.bulk((any(BulkRequest.class)))).willReturn(mock);


        assertDoesNotThrow(() -> new ElasticEngineImpl(client).indexBulk(movies));
    }

    @Test
    void givenMovies_whenBulkIndex_thenBulkIndexException() throws IOException {
        BulkResponse mock = mock(BulkResponse.class);
        given(mock.errors()).willReturn(true);
        given(client.bulk((any(BulkRequest.class)))).willReturn(mock);


        Exception exception = assertThrows(BulkIndexException.class, () -> new ElasticEngineImpl(client).indexBulk(movies));

        String expectedMessage = "Error indexing bulk";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void givenMovies_whenBulkIndex_thenIOException() throws IOException {
        given(client.bulk((any(BulkRequest.class)))).willThrow(IOException.class);
        assertThrows(IOException.class, () -> new ElasticEngineImpl(client).indexBulk(movies));
    }


}