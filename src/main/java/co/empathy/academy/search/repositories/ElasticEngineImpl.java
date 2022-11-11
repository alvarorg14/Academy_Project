package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ElasticEngineImpl implements ElasticEngine {

    private static final String INDEX_NAME = "imdb";
    private final ElasticsearchClient client;

    public ElasticEngineImpl(ElasticsearchClient client) {
        this.client = client;
    }

    /*
    @Override
    public void makeAggsQuery() throws IOException {
        Query query = BoolQuery.of(b -> b
                .filter(multiMatch("avengers", new String[]{"primaryTitle"})))._toQuery();

        Aggregation genres = TermsAggregation.of(t -> t.field("genres").size(100))._toAggregation();
        Aggregation types = TermsAggregation.of(t -> t.field("titleType").size(100))._toAggregation();

        Aggregation ranking = RangeAggregation.of(r -> r.field("averageRating").ranges(
                AggregationRange.of(a -> a.key("No ranking").from("0.0").to("0.0001")),
                AggregationRange.of(a -> a.key("0.0-2.0").from("0.1").to("2.0")),
                AggregationRange.of(a -> a.key("2.0-4.0").from("2.0").to("4.0")),
                AggregationRange.of(a -> a.key("4.0-6.0").from("4.0").to("6.0")),
                AggregationRange.of(a -> a.key("6.0-8.0").from("6.0").to("8.0")),
                AggregationRange.of(a -> a.key("8.0-10.0").from("8.0").to("10.0"))
        ))._toAggregation();

        Map<String, Aggregation> aggs = new HashMap<String, Aggregation>();
        aggs.put("genres", genres);
        aggs.put("types", types);
        aggs.put("ranking", ranking);

        SearchResponse<Movie> response = client.search(b -> b
                .index(INDEX_NAME)
                .size(100)
                .query(query)
                .aggregations(aggs), Movie.class);

        //Print the movies
        System.out.println("Movies:");
        for (Hit<Movie> hit : response.hits().hits()) {
            System.out.println(hit.source());
        }

        System.out.println("Genres");
        Aggregate genresAgg = response.aggregations().get("genres");
        genresAgg.sterms().buckets().array().forEach(bucket -> {
            System.out.println(bucket.key() + " " + bucket.docCount());
        });
        System.out.println("Types");
        response.aggregations().get("types").sterms().buckets().array().forEach(bucket -> {
            System.out.println(bucket.key() + " " + bucket.docCount());
        });
        System.out.println("Ranking");
        response.aggregations().get("ranking").range().buckets().array().forEach(bucket -> {
            System.out.println(bucket.key() + " " + bucket.docCount());
        });
    }*/


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


}
