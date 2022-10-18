package co.empathy.academy.search.repositories;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ElasticEngineImpl implements ElasticEngine {

    private RestClient client;

    public ElasticEngineImpl(RestClient client) {
        this.client = client;
    }

    /**
     * Makes a petition to elastic search, similar to making a request to http://localhost:9200
     * @return elasticsearch information
     */
    @Override
    public String getElasticInfo() {
        Request request = new Request("GET", "/");
        try {
            Response response = client.performRequest(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
