package co.empathy.academy.search.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticEngineImpl;
import co.empathy.academy.search.repositories.ElasticLowClient;
import co.empathy.academy.search.repositories.ElasticLowClientImpl;
import co.empathy.academy.search.repositories.names.ElasticNamesEngine;
import co.empathy.academy.search.repositories.names.ElasticNamesEngineImpl;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoriesConfig {

    @Bean
    public ElasticLowClient getElasticLowClient(RestClient restClient) {
        return new ElasticLowClientImpl(restClient);
    }

    @Bean
    public ElasticEngine getElasticEngine(ElasticsearchClient client) {
        return new ElasticEngineImpl(client);
    }

    @Bean
    public ElasticNamesEngine getElasticNamesEngine(ElasticsearchClient client) {
        return new ElasticNamesEngineImpl(client);
    }
}
