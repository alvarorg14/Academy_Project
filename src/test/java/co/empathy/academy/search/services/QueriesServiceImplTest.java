package co.empathy.academy.search.services;

import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticLowClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class QueriesServiceImplTest {

    @Test
    void givenQuery_whenSearch_thenQueryResponse() {
        String query = "query";
        ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
        given(elasticLowClient.getElasticInfo()).willReturn("{\"cluster_name\":\"docker-cluster\"}");

        ElasticEngine elasticEngine = mock(ElasticEngine.class);

        QueriesService queriesService = new QueriesServiceImpl(elasticLowClient, elasticEngine);

        QueryResponse response = queriesService.search(query);

        assertEquals(new QueryResponse(query, "docker-cluster"), response);
    }

    @Test
    void givenQueryAndElasticDown_whenSearch_thenRuntimeException() {
        String query = "query";

        ElasticLowClient elasticLowClient = mock(ElasticLowClient.class);
        given(elasticLowClient.getElasticInfo()).willThrow(new RuntimeException());

        ElasticEngine elasticEngine = mock(ElasticEngine.class);
        QueriesService queriesService = new QueriesServiceImpl(elasticLowClient, elasticEngine);

        assertThrows(RuntimeException.class, () -> queriesService.search(query));
    }
}