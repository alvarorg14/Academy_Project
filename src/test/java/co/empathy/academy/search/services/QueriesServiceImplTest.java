package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueriesServiceImplTest {

    private final QueriesService queriesService = new QueriesServiceImpl();

    private final String query = "query";
    private final String field = "field";

    @Test
    void givenQueryAndField_whenMultiMatch_thenQueryFormed() {
        Query result = queriesService.multiMatch(query, new String[]{field, "field2"});
        assertNotNull(result);
        assertTrue(result.isMultiMatch());
    }

    @Test
    void givenQueryAndField_whenTermQuery_thenQueryFormed() {
        Query result = queriesService.termQuery(query, field);
        assertNotNull(result);
        assertTrue(result.isTerm());
    }

    @Test
    void givenQueryAndField_whenTermsQuery_thenQueryFormed() {
        Query result = queriesService.termsQuery(new String[]{query, "query2"}, field);
        assertNotNull(result);
        assertTrue(result.isTerms());
    }

    @Test
    void givenQueries_whenBoolQuery_thenQueryFormed() {
        Query result = queriesService.boolQuery(
                new ArrayList<>() {{
                    add(queriesService.multiMatch(query, new String[]{field, "field2"}));
                    add(queriesService.termQuery(query, field));
                }}
        );
        assertNotNull(result);
        assertTrue(result.isBool());
    }

    @Test
    void givenQueries_whenShouldQuery_thenQueryFormed() {
        Query result = queriesService.shouldQuery(
                new ArrayList<>() {{
                    add(queriesService.multiMatch(query, new String[]{field, "field2"}));
                    add(queriesService.termQuery(query, field));
                }}
        );
        assertNotNull(result);
        assertTrue(result.isBool());
    }

    @Test
    void givenFieldAndRange_whenRangeDoubleQuery_thenQueryFormed() {
        Query result = queriesService.rangeDoubleQuery(field, 1.0, 2.0);
        assertNotNull(result);
        assertTrue(result.isRange());
    }

    @Test
    void givenFieldAndRange_whenRangeIntegerQuery_thenQueryFormed() {
        Query result = queriesService.rangeIntegerQuery(field, 1, 2);
        assertNotNull(result);
        assertTrue(result.isRange());
    }

}