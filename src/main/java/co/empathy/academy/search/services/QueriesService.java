package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.QueryResponse;

public interface QueriesService {

    QueryResponse search(String query);
}
