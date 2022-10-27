package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticEngineImpl implements ElasticEngine {

    private static final String INDEX_NAME = "movies";
    private final ElasticsearchClient client;

    public ElasticEngineImpl(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * Creates an index in the cluster using the elastic client.
     *
     * @param name - name of the index
     * @throws Exception - if the index cannot be created
     */
    @Override
    public void createIndex(String name) throws IOException {
        try {
            client.indices().delete(d -> d.index(INDEX_NAME));
        } catch (Exception e) {
            // Ignore
        }

        client.indices().create(c -> c.index(name));
    }

    /**
     * Puts the settings of the index
     *
     * @throws IOException - If the settings cannot be loaded
     */
    @Override
    public void putSettings() throws IOException {
        client.indices().close(c -> c.index(INDEX_NAME));

        InputStream analyzer = getClass().getClassLoader().getResourceAsStream("my_standard_analyzer.json");
        client.indices().putSettings(p -> p.index(INDEX_NAME).withJson(analyzer));

        client.indices().open(o -> o.index(INDEX_NAME));
    }

    /**
     * Puts the mapping of the index
     *
     * @throws IOException If the mapping cannot be loaded
     */
    @Override
    public void putMapping() throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream("mapping.json");
        client.indices().putMapping(p -> p.index(INDEX_NAME).withJson(mapping));
    }

    /**
     * Indexes a document
     *
     * @param indexName - name of the index where the document will be indexed
     * @param movie     - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    @Override
    public void indexDocument(String indexName, Movie movie) throws IOException {
        client.index(i -> i
                .index(indexName)
                .id(movie.getTconst())
                .document(movie));
    }

    /**
     * Performs a multi match query
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return - list of movies that match the query
     */
    @Override
    public List<Movie> multiMatch(String query, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(query)
                .fields(Arrays.stream(fields).toList()))._toQuery();
        try {
            SearchResponse<Movie> response = client.search(s -> s
                    .index(INDEX_NAME)
                    .query(multiMatchQuery), Movie.class);

            List<Movie> movies = response.hits().hits().stream()
                    .map(h -> h.source())
                    .toList();

            return movies;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs a term query
     *
     * @param field Field to search
     * @param value Value to search
     * @return List of movies that match the query
     */
    @Override
    public List<Movie> termQuery(String value, String field) {
        Query termQuery = TermQuery.of(t -> t
                .value(value)
                .field(field))._toQuery();
        try {
            SearchResponse<Movie> response = client.search(s -> s
                    .index(INDEX_NAME)
                    .query(termQuery), Movie.class);

            List<Movie> movies = response.hits().hits().stream()
                    .map(h -> h.source())
                    .toList();

            return movies;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs a terms query
     *
     * @param values Values to search
     * @param field  Field to search
     * @return List of movies that match the query
     */
    @Override
    public List<Movie> termsQuery(String[] values, String field) {
        TermsQueryField termsQueryField = TermsQueryField.of(t -> t
                .value(Arrays.stream(values).toList().stream().map(FieldValue::of).collect(Collectors.toList())));

        Query termsQuery = TermsQuery.of(t -> t
                .field(field)
                .terms(termsQueryField))._toQuery();

        try {
            SearchResponse<Movie> response = client.search(s -> s
                    .index(INDEX_NAME)
                    .query(termsQuery), Movie.class);

            List<Movie> movies = response.hits().hits().stream()
                    .map(h -> h.source())
                    .toList();

            return movies;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
