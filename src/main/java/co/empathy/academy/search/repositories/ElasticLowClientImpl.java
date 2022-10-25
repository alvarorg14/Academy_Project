package co.empathy.academy.search.repositories;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ElasticLowClientImpl implements ElasticLowClient {

    private final RestClient lowClient;

    public ElasticLowClientImpl(RestClient lowClient) {
        this.lowClient = lowClient;
    }

    /**
     * Makes a petition to elastic search, similar to making a request to http://localhost:9200
     *
     * @return elasticsearch information
     */
    @Override
    public String getElasticInfo() {
        Request request = new Request("GET", "/");
        try {
            Response response = lowClient.performRequest(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
