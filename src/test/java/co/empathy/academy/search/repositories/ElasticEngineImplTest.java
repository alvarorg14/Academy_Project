package co.empathy.academy.search.repositories;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import javax.websocket.RemoteEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ElasticEngineImplTest {

    @Test
    void givenElasticUp_whenGetElasticInfo_thenElasticInfoReturned() throws IOException {
        String expectedResponse = "{\"cluster_name\":\"docker-cluster\"}";

        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(expectedResponse.getBytes()));

        Response mockResponse = mock(Response.class);
        given(mockResponse.getEntity()).willReturn(entity);

        RestClient client = mock(RestClient.class);
        given(client.performRequest(any())).willReturn(mockResponse);

        ElasticEngineImpl elasticEngine = new ElasticEngineImpl(client);

        String response = elasticEngine.getElasticInfo();

        assertEquals(expectedResponse, response);
    }

    @Test
    void givenElasticUp_whenGetElasticInfo_thenRuntimeExcpetion() throws IOException {
        RestClient client = mock(RestClient.class);
        given(client.performRequest(any())).willThrow(new IOException());

        ElasticEngine elasticEngine = new ElasticEngineImpl(client);

        assertThrows(RuntimeException.class, () -> elasticEngine.getElasticInfo());
    }
}