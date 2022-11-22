package co.empathy.academy.search.models.facets;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FacetTest {

    @Test
    void givenAttributes_whenConstructor_thenFacetCreated() {
        List<FacetValue> values = List.of(new FacetValue("id1", "value1", 1L, "filter1"),
                new FacetValue("id2", "value2", 2L, "filter2"));

        Facet facet = new Facet("facet", "type", values);

        assertEquals("facet", facet.getFacet());
        assertEquals("type", facet.getType());
        assertEquals(2, facet.getValues().size());
        assertEquals("id1", facet.getValues().get(0).getId());
        assertEquals("id2", facet.getValues().get(1).getId());
    }

}