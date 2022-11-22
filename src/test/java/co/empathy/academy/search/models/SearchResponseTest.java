package co.empathy.academy.search.models;

import co.empathy.academy.search.models.facets.Facet;
import co.empathy.academy.search.models.facets.FacetValue;
import co.empathy.academy.search.util.ResourcesUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SearchResponseTest {

    @Test
    void givenAttributes_whenConstructor_thenSearchResponseCreated() {
        List<Movie> hits = ResourcesUtil.getMovies();
        List<Facet> facets = List.of(new Facet("facet", "value",
                List.of(new FacetValue("id", "value", 1L, "filter"))));
        SearchResponse searchResponse = new SearchResponse(hits, facets);
        assertEquals(hits, searchResponse.getHits());
        assertEquals(facets, searchResponse.getFacets());
        assertEquals("test1", searchResponse.getHits().get(0).getTconst());
        assertEquals("test2", searchResponse.getHits().get(1).getTconst());
        assertEquals("facet", searchResponse.getFacets().get(0).getFacet());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenSearchResponseCreated() {
        SearchResponse searchResponse = new SearchResponse();
        assertNull(searchResponse.getHits());
        assertNull(searchResponse.getFacets());
    }

}