package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ElasticEngineImplTest {

    ElasticsearchClient client = mock(ElasticsearchClient.class);

    @Test
    void givenQueryAndFields_whenMultiMatchQuery_thenQueryFormed() {
        String query = "query";
        String[] fields = {"field1", "field2"};

        String expectedQuery = "Query: {\"multi_match\":{\"fields\":[\"field1\",\"field2\"],\"query\":\"query\"}}";

        Query multiMatch = new ElasticEngineImpl(client).multiMatch(query, fields);

        assertTrue(multiMatch.isMultiMatch());
        assertEquals(expectedQuery, multiMatch.toString());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenQueryFormed() {
        String query = "query";
        String field = "field";

        String expectedQuery = "Query: {\"term\":{\"field\":{\"value\":\"query\"}}}";

        Query termQuery = new ElasticEngineImpl(client).termQuery(query, field);

        assertTrue(termQuery.isTerm());
        assertEquals(expectedQuery, termQuery.toString());
    }

    @Test
    void givenQueriesAndField_whenTermsQuery_thenQueryFormed() {
        String[] queries = {"query1", "query2"};
        String field = "field";

        String expectedQuery = "Query: {\"terms\":{\"field\":[\"query1\",\"query2\"]}}";

        Query termsQuery = new ElasticEngineImpl(client).termsQuery(queries, field);

        assertTrue(termsQuery.isTerms());
        assertEquals(expectedQuery, termsQuery.toString());
    }

    /*
    @Test
    void givenQuery_whenPerformQuery_thenMoviesReturned() throws IOException {
        ElasticEngine elasticEngine = new ElasticEngineImpl(client);

        Movie movie = new Movie("tTest", "testType", "testTitle",
                "testOriginalTitle", false, "testStartYear",
                "testEndYear", 100, "testGenres");

        Query multiMatch = elasticEngine.multiMatch("test", new String[]{"primaryTitle", "originalTitle"});

        Hit<Movie> hit = Hit.of(movieBuilder -> movieBuilder
                .source(movie)
                .index("testIndex")
                .id(movie.getTconst()));

        List<Hit<Movie>> hits = new ArrayList<>();
        hits.add(hit);

        HitsMetadata<Movie> hitsMetadata = HitsMetadata.of(b -> b.hits(hits));

        SearchResponse<Movie> response = mock(SearchResponse.class);
        given(response.hits().hits().stream()).willReturn(hitsMetadata.hits().stream());


        ElasticsearchClient client = mock(ElasticsearchClient.class);
        given(client.search(c -> c.index("TestIndex").query(multiMatch), Movie.class)).willReturn(response);

        List<Movie> movies = elasticEngine.performQuery(multiMatch);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(client, times(1)).search(c -> c.index("TestIndex").query(multiMatch), Movie.class);
    }*/
}