package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;

public interface QueriesService {

    /**
     * Creates a multi match query
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return Query to be executed
     */
    Query multiMatch(String query, String[] fields);

    /**
     * Performs a term query
     *
     * @param field - field to search
     * @param value - value to search
     * @return Query to be executed
     */
    Query termQuery(String value, String field);

    /**
     * Creates a list of term queries
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of term queries
     */
    List<Query> termQueries(String[] values, String field);

    /**
     * Performs a terms query
     *
     * @param values Values to search
     * @param field  Field to search
     * @return Query to be executed
     */
    Query termsQuery(String[] values, String field);

    /**
     * Creates a bool query
     *
     * @param queries List of queries to be executed
     * @return Query to be executed
     */
    Query boolQuery(List<Query> queries);

    /**
     * Creates a should query
     *
     * @param queries List of queries to be executed
     * @return Query to be executed
     */
    Query shouldQuery(List<Query> queries);

    /**
     * Creates a range double query
     *
     * @param field Field to search
     * @param min   Min value
     * @param max   Max value
     * @return Query to be executed
     */
    Query rangeDoubleQuery(String field, Double min, Double max);

    /**
     * Creates a range integer query
     *
     * @param field Field to search
     * @param min   Min value
     * @param max   Max value
     * @return Query to be executed
     */
    Query rangeIntegerQuery(String field, Integer min, Integer max);

    /**
     * Creates the sort options for the query
     *
     * @param field Field to sort
     * @param order Ascending or descending
     * @return Sort option
     */
    SortOptions sort(String field, String order);
}
