package co.empathy.academy.search.repositories;

import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ElasticEngineImplTest {

    @Test
    void givenElasticUp_whenGetElasticInfo_thenElasticInfoReturned() throws IOException {
        RestClient client = mock(RestClient.class);
        given(client.performRequest(any())).willReturn(null); //TODO

        //TODO
    }
}