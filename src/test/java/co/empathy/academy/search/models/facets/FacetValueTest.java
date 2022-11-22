package co.empathy.academy.search.models.facets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FacetValueTest {

    @Test
    void givenAttributes_whenConstructor_thenFacetValueCreated() {
        FacetValue facetValue = new FacetValue("id", "value", 1L, "filter");
        assertEquals("id", facetValue.getId());
        assertEquals("value", facetValue.getValue());
        assertEquals(1L, facetValue.getCount());
        assertEquals("filter", facetValue.getFilter());
    }

}