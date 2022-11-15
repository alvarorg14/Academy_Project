package co.empathy.academy.search.models;

import co.empathy.academy.search.models.facets.Facet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SearchResponse {

    List<Movie> hits;
    List<Facet> facets;
}
