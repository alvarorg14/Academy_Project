package co.empathy.academy.search.controllers;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.academy.search.entities.QueryResponse;
import co.empathy.academy.search.services.QueriesService;
import jakarta.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
public class QueriesController {

    private QueriesService queriesService;

    public QueriesController(QueriesService queriesService) {
        this.queriesService = queriesService;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResponse search(@RequestParam("query") String query) throws ParseException, IOException {
        return queriesService.search(query);
    }
}
