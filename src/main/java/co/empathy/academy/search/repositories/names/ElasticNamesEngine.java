package co.empathy.academy.search.repositories.names;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Name;

import java.io.IOException;
import java.util.List;

public interface ElasticNamesEngine {

    /**
     * Indexes a bulk of names
     *
     * @param names list of names to be indexed
     */
    void indexBulk(List<Name> names) throws IOException, BulkIndexException;

    /**
     * Creates an index using the elastic client
     *
     * @throws IOException - if the index cannot be created
     */
    void createIndex() throws IOException;

    /**
     * Puts the mapping of the index
     *
     * @throws IOException If the mapping cannot be loaded
     */
    void putMapping() throws IOException;

    /**
     * Makes a query to elasticsearch
     *
     * @param query Query to make
     * @return List of names that match the query
     */
    List<Name> performQuery(Query query) throws IOException;
}
