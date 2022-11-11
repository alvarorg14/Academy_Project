package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueriesServiceImpl implements QueriesService {

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
     * Creates a bool query
     *
     * @param queries List of queries to be executed
     * @return Query to be executed
     */
    @Override
    public Query boolQuery(List<Query> queries) {
        Query boolQuery = BoolQuery.of(b -> b.filter(queries))._toQuery();
        return boolQuery;
    }

    /**
     * Creates a should query
     *
     * @param queries List of queries to be executed
     * @return Query to be executed
     */
    @Override
    public Query shouldQuery(List<Query> queries) {
        Query shouldQuery = BoolQuery.of(b -> b.should(queries))._toQuery();
        return shouldQuery;
    }

    /**
     * Creates a range double filter
     *
     * @param field Field to search
     * @param min   Min value
     * @param max   Max value
     * @return Query to be executed
     */
    @Override
    public Query rangeDoubleQuery(String field, Double min, Double max) {
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(min))
                .lte(JsonData.of(max)))._toQuery();
    }

    /**
     * Creates a range integer filter
     *
     * @param field Field to search
     * @param min   Min value
     * @param max   Max value
     * @return Query to be executed
     */
    @Override
    public Query rangeIntegerQuery(String field, Integer min, Integer max) {
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(min))
                .lte(JsonData.of(max)))._toQuery();
    }
}
