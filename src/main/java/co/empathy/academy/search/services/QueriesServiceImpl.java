package co.empathy.academy.search.services;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticLowClient;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.List;

public class QueriesServiceImpl implements QueriesService {

    private final ElasticLowClient elasticLowClient;

    private final ElasticEngine elasticEngine;

    public QueriesServiceImpl(ElasticLowClient elasticLowClient, ElasticEngine elasticEngine) {
        this.elasticLowClient = elasticLowClient;
        this.elasticEngine = elasticEngine;
    }

    @Override
    public void makeAggsQuery() throws IOException {
        elasticEngine.makeAggsQuery();
    }

    /**
     * Makes a request to obtain the cluster name and returns it and the query performed.
     *
     * @param query
     * @return QueryResponse
     */
    @Override
    public QueryResponse search(String query) {
        String elasticInfo = elasticLowClient.getElasticInfo();
        //Parse the json above to obtain the cluster name
        String clusterName = "";
        try {
            clusterName = new JSONParser(elasticInfo).parseObject().get("cluster_name").toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new QueryResponse(query, clusterName);
    }

    /**
     * Performs a multi match query to the movies index
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return List of movies
     */
    @Override
    public List<Movie> multiMatch(String query, String fields) throws IOException {
        String[] fieldsArray = fields.split(",");
        return elasticEngine.performQuery(elasticEngine.multiMatch(query, fieldsArray));
    }

    /**
     * Performs a term query to the movies index
     *
     * @param value - value to search
     * @param field - field to search
     * @return List of movies
     */
    @Override
    public List<Movie> termQuery(String value, String field) throws IOException {
        return elasticEngine.performQuery(elasticEngine.termQuery(value, field));
    }

    /**
     * Performs a terms query to the movies index
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of movies
     */
    @Override
    public List<Movie> termsQuery(String values, String field) throws IOException {
        String[] valuesArray = values.split(",");
        return elasticEngine.performQuery(elasticEngine.termsQuery(valuesArray, field));
    }
}
