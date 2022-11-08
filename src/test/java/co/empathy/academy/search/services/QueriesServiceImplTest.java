package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticLowClient;
import co.empathy.academy.search.util.ResourcesUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueriesServiceImplTest {

    private final ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
    private final ElasticEngine elasticEngine = mock(ElasticEngine.class);

    private final QueriesService queriesService = new QueriesServiceImpl(elasticLowClient, elasticEngine);

    private final Movie movie = ResourcesUtil.getMovie("");

    @Test
    void givenQuery_whenSearch_thenQueryResponse() {
        String query = "query";
        ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
        given(elasticLowClient.getElasticInfo()).willReturn("{\"cluster_name\":\"docker-cluster\"}");

        ElasticEngine elasticEngine = mock(ElasticEngine.class);

        QueriesService queriesService = new QueriesServiceImpl(elasticLowClient, elasticEngine);

        QueryResponse response = queriesService.search(query);

        assertEquals(new QueryResponse(query, "docker-cluster"), response);
    }

    @Test
    void givenQueryAndElasticDown_whenSearch_thenRuntimeException() {
        String query = "query";

        ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
        given(elasticLowClient.getElasticInfo()).willThrow(new RuntimeException());

        ElasticEngine elasticEngine = mock(ElasticEngine.class);
        QueriesService queriesService = new QueriesServiceImpl(elasticLowClient, elasticEngine);

        assertThrows(RuntimeException.class, () -> queriesService.search(query));
    }

    @Test
    void givenQueryAndFields_whenMultiMatchQuery_thenMoviesReturned() throws IOException {
        String query = "query";
        String fields = "field1,field2";

        given(elasticEngine.performQuery(any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = queriesService.multiMatch(query, fields);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(elasticEngine, times(1)).multiMatch(query, fields.split(","));
        verify(elasticEngine, times(1)).performQuery(any());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenMoviesReturned() throws IOException {
        String query = "query";
        String field = "field1";
        given(elasticEngine.performQuery(any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = queriesService.termQuery(query, field);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(elasticEngine, times(1)).termQuery(query, field);
        verify(elasticEngine, times(1)).performQuery(any());
    }

    @Test
    void givenQueriesAndFields_whenTermsQuery_thenMoviesReturned() throws IOException {
        String queries = "query1,query2";
        String field = "field1";
        given(elasticEngine.performQuery(any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = queriesService.termsQuery(queries, field);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(elasticEngine, times(1)).termsQuery(queries.split(","), field);
        verify(elasticEngine, times(1)).performQuery(any());
    }
}