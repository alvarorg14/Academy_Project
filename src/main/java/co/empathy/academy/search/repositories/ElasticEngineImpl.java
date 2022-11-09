package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticEngineImpl implements ElasticEngine {

    private static final String INDEX_NAME = "imdb";
    private final ElasticsearchClient client;

    public ElasticEngineImpl(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * Creates an index in the cluster using the elastic client.
     *
     * @throws IOException - if the index cannot be created
     */
    @Override
    public void createIndex() throws IOException {
        try {
            client.indices().delete(d -> d.index(INDEX_NAME));
        } catch (Exception e) {
            // Ignore
        }
        client.indices().create(c -> c.index(INDEX_NAME));
    }

    /**
     * Puts the settings of the index
     *
     * @throws IOException - If the settings cannot be loaded
     */
    @Override
    public void putSettings() throws IOException {
        client.indices().close(c -> c.index(INDEX_NAME));

        InputStream analyzer = getClass().getClassLoader().getResourceAsStream("custom_analyzer.json");
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
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    @Override
    public void indexDocument(Movie movie) throws IOException {
        client.index(i -> i
                .index(INDEX_NAME)
                .id(movie.getTconst())
                .document(movie));
    }

    /**
     * Creates a multimatch query
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return Query
     */
    @Override
    public Query multiMatch(String query, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(query)
                .fields(Arrays.stream(fields).toList()))._toQuery();

        return multiMatchQuery;
    }

    /**
     * Creates a term query
     *
     * @param field Field to search
     * @param value Value to search
     * @return Query
     */
    @Override
    public Query termQuery(String value, String field) {
        Query termQuery = TermQuery.of(t -> t
                .value(value)
                .field(field))._toQuery();

        return termQuery;
    }

    /**
     * Creates a terms query
     *
     * @param values Values to search
     * @param field  Field to search
     * @return Query
     */
    @Override
    public Query termsQuery(String[] values, String field) {
        TermsQueryField termsQueryField = TermsQueryField.of(t -> t
                .value(Arrays.stream(values).toList().stream().map(FieldValue::of).collect(Collectors.toList())));

        Query termsQuery = TermsQuery.of(t -> t
                .field(field)
                .terms(termsQueryField))._toQuery();

        return termsQuery;
    }

    /**
     * Performs a query to elasticsearch
     *
     * @param query Query to make
     * @return List of movies that match the query
     */
    @Override
    public List<Movie> performQuery(Query query) throws IOException {
        SearchResponse<Movie> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(query)
                .size(100), Movie.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();

    }

    /**
     * Indexes a list of movies
     *
     * @param movies Movies to index
     * @return True if the movies were indexed, false otherwise
     */
    @Override
    public void indexBulk(List<Movie> movies) throws IOException, BulkIndexException {
        BulkRequest.Builder request = new BulkRequest.Builder();

        movies.forEach(movie -> request.operations(op -> op
                .index(i -> i
                        .index(INDEX_NAME)
                        .id(movie.getTconst())
                        .document(movie))));

        BulkResponse bulkResponse = client.bulk(request.build());
        if (bulkResponse.errors()) {
            throw new BulkIndexException("Error indexing bulk");
        }
    }


}
