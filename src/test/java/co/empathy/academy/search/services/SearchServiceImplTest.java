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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    private final ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
    private final ElasticEngine elasticEngine = mock(ElasticEngine.class);

    private final QueriesService queriesService = mock(QueriesService.class);

    private final SearchService searchService = new SearchServiceImpl(elasticLowClient, elasticEngine, queriesService);

    private final Movie movie = ResourcesUtil.getMovie("");

    @Test
    void givenQuery_whenSearch_thenQueryResponse() {
        String query = "query";
        ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
        given(elasticLowClient.getElasticInfo()).willReturn("{\"cluster_name\":\"docker-cluster\"}");

        ElasticEngine elasticEngine = mock(ElasticEngine.class);

        SearchService searchService = new SearchServiceImpl(elasticLowClient, elasticEngine, queriesService);

        QueryResponse response = searchService.search(query);

        assertEquals(new QueryResponse(query, "docker-cluster"), response);
    }

    @Test
    void givenQueryAndElasticDown_whenSearch_thenRuntimeException() {
        String query = "query";

        ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
        given(elasticLowClient.getElasticInfo()).willThrow(new RuntimeException());

        ElasticEngine elasticEngine = mock(ElasticEngine.class);
        SearchService searchService = new SearchServiceImpl(elasticLowClient, elasticEngine, queriesService);

        assertThrows(RuntimeException.class, () -> searchService.search(query));
    }


    @Test
    void givenQueryAndFields_whenMultiMatchQuery_thenMoviesReturned() throws IOException {
        String query = "query";
        String fields = "field1,field2";

        given(elasticEngine.performQuery(any(), any(), any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = searchService.multiMatch(query, fields);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(queriesService, times(1)).multiMatch(query, fields.split(","));
        verify(elasticEngine, times(1)).performQuery(any(), any(), any());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenMoviesReturned() throws IOException {
        String query = "query";
        String field = "field1";
        given(elasticEngine.performQuery(any(), any(), any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = searchService.termQuery(query, field);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(queriesService, times(1)).termQuery(query, field);
        verify(elasticEngine, times(1)).performQuery(any(), any(), any());
    }

    @Test
    void givenQueriesAndFields_whenTermsQuery_thenMoviesReturned() throws IOException {
        String queries = "query1,query2";
        String field = "field1";
        given(elasticEngine.performQuery(any(), any(), any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = searchService.termsQuery(queries, field);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(queriesService, times(1)).termsQuery(queries.split(","), field);
        verify(elasticEngine, times(1)).performQuery(any(), any(), any());
    }

    @Test
    void givenAllFilters_whenAllFiltersSearch_thenMoviesReturned() throws IOException {
        given(elasticEngine.performQuery(any(), any(), any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = searchService.allFiltersSearch(Optional.of("genre1,genre2"),
                Optional.of("type1,type2"), Optional.of(2017), Optional.of(2016),
                Optional.of(90), Optional.of(0), Optional.of(10.0), Optional.of(5.0),
                Optional.of(10), Optional.of("asc"));

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(queriesService, times(2)).termQueries(any(), any());
        verify(queriesService, times(2)).rangeIntegerQuery(any(), any(), any());
        verify(queriesService, times(1)).rangeDoubleQuery(any(), any(), any());
        verify(queriesService, times(2)).shouldQuery(any());
        verify(queriesService, times(1)).boolQuery(any());
        verify(elasticEngine, times(1)).performQuery(any(), any(), any());
    }
}