package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.QueryResponse;
import co.empathy.academy.search.services.QueriesService;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class QueriesControllerTest {

    @Test
    void givenQuery_whenSearch_thenQueryResponse() throws ParseException, IOException {
        String query = "Query";
        QueryResponse response = new QueryResponse(query, "clusterName");

        QueriesService queriesService = mock(QueriesService.class);
        given(queriesService.search(query)).willReturn(response);

        QueriesController queriesController = new QueriesController(queriesService);

        QueryResponse result = queriesController.search(query);

        assertEquals(response, result);
    }
}