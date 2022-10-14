package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.QueryResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
public class QueriesController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResponse search(@RequestParam("query") String query) throws ParseException {
        JSONParser parser = new JSONParser(restTemplate.getForObject("http://localhost:9200", String.class));
        String clusterName = "";
        try {
            clusterName =  ((LinkedHashMap) parser.parse()).get("cluster_name").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        QueryResponse response = new QueryResponse(query, clusterName);
        return response;
    }
}
