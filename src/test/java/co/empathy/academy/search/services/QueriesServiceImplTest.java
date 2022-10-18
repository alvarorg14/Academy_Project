package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.QueryResponse;
import co.empathy.academy.search.repositories.ElasticEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class QueriesServiceImplTest {

    @Test
    void givenQuery_whenSearch_thenQueryResponse(){
        String query = "query";
        ElasticEngine elasticEngine = mock(ElasticEngine.class);
        given(elasticEngine.getElasticInfo()).willReturn("{\"cluster_name\":\"docker-cluster\"}");

        QueriesService queriesService = new QueriesServiceImpl(elasticEngine);

        QueryResponse response = queriesService.search(query);

        assertEquals(new QueryResponse(query, "docker-cluster"), response);
    }
}