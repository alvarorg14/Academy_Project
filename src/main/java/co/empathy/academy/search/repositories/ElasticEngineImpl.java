package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.facets.Facet;
import co.empathy.academy.search.models.facets.FacetValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticEngineImpl implements ElasticEngine {

    private static final String INDEX_NAME = "imdb";
    private final ElasticsearchClient client;

    public ElasticEngineImpl(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * Performs a query to elasticsearch
     *
     * @param query    Query to make
     * @param maxNHits Maximum number of hits to return
     * @return List of movies that match the query
     */
    @Override
    public List<Movie> performQuery(Query query, Integer maxNHits) throws IOException {
        SearchResponse<Movie> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(query)
                .size(maxNHits), Movie.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();

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

    /**
     * Gets a genres aggregation
     *
     * @return Facet with the genres aggregation
     */
    @Override
    public Facet getGenresAggregation() throws IOException {
        Query query = BoolQuery.of(b -> b
                .filter(MatchAllQuery.of(q -> q.queryName("MatchAll"))._toQuery()))._toQuery();

        Aggregation genres = TermsAggregation.of(t -> t.field("genres").size(100))._toAggregation();
        Map<String, Aggregation> aggs = new HashMap<String, Aggregation>();
        aggs.put("genres", genres);

        SearchResponse<Movie> response = client.search(b -> b
                .index(INDEX_NAME)
                .size(1000)
                .query(query)
                .aggregations(aggs), Movie.class);

        List<FacetValue> values = new ArrayList<>();
        Aggregate genresAgg = response.aggregations().get("genres");
        genresAgg.sterms().buckets().array().forEach(bucket -> {
            values.add(new FacetValue(bucket.key().toLowerCase(), bucket.key().toLowerCase(),
                    bucket.docCount(), "genres:" + bucket.key().toLowerCase()));
        });
        return new Facet("facetGenres", "values", values);
    }


}
