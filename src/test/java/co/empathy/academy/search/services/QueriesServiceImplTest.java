package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Query result = queriesService.mustQuery(
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

    @Test
    void givenFieldAndValues_whenTermQueries_thenListQueriesFormed() {
        List<Query> result = queriesService.termQueries(new String[]{query, "query2"}, field);

        assertEquals(2, result.size());
        assertTrue(result.get(0).isTerm());
        assertTrue(result.get(1).isTerm());
    }

    @Test
    void givenFieldAndAscOrder_whenSort_thenSortOptionFormed() {
        SortOptions result = queriesService.sort(field, "asc");
        assertNotNull(result);
        assertTrue(result.isField());
        assertEquals(SortOrder.Asc, result.field().order());
    }

    @Test
    void givenFieldAndDescOrder_whenSort_thenSortOptionFormed() {
        SortOptions result = queriesService.sort(field, "desc");
        assertNotNull(result);
        assertTrue(result.isField());
        assertEquals(SortOrder.Desc, result.field().order());
    }


}