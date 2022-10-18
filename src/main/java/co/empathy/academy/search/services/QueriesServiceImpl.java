package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.QueryResponse;
import co.empathy.academy.search.repositories.ElasticEngine;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

public class QueriesServiceImpl implements QueriesService {

    private ElasticEngine elasticEngine;

    public QueriesServiceImpl(ElasticEngine elasticEngine){
        this.elasticEngine = elasticEngine;
    }

    /**
     * Makes a request to obtain the cluster name and returns it and the query performed.
     * @param query
     * @return QueryResponse
     */
    @Override
    public QueryResponse search(String query) {
        String elasticInfo = elasticEngine.getElasticInfo();
        //Parse the json above to obtain the cluster name
        String clusterName = "";
        try {
            clusterName = new JSONParser(elasticInfo).parseObject().get("cluster_name").toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new QueryResponse(query, clusterName);
    }
}
