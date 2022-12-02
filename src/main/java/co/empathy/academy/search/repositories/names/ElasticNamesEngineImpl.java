package co.empathy.academy.search.repositories.names;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Name;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ElasticNamesEngineImpl implements ElasticNamesEngine {

    private static final String INDEX_NAME = "names";
    private final ElasticsearchClient client;

    public ElasticNamesEngineImpl(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * Indexes a bulk of names
     *
     * @param names list of names to be indexed
     */
    @Override
    public void indexBulk(List<Name> names) throws IOException, BulkIndexException {
        BulkRequest.Builder request = new BulkRequest.Builder();

        names.forEach(name -> request.operations(op -> op
                .index(i -> i
                        .index(INDEX_NAME)
                        .id(name.getNconst())
                        .document(name))));

        BulkResponse bulkResponse = client.bulk(request.build());
        if (bulkResponse.errors()) {
            throw new BulkIndexException("Error indexing bulk");
        }
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
     * Puts the mapping of the index
     *
     * @throws IOException If the mapping cannot be loaded
     */
    @Override
    public void putMapping() throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream("namesMapping.json");
        client.indices().putMapping(p -> p.index(INDEX_NAME).withJson(mapping));
    }

    /**
     * Makes a query to elasticsearch
     *
     * @param query Query to make
     * @return List of names that match the query
     */
    @Override
    public List<Name> performQuery(Query query) throws IOException {
        SearchResponse<Name> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(query), Name.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }
}
