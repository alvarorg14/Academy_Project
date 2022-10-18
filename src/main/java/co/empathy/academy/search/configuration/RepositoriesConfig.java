package co.empathy.academy.search.configuration;

import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticEngineImpl;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoriesConfig {

    @Bean
    public ElasticEngine getElasticEngine(RestClient restClient) {
       return new ElasticEngineImpl(restClient);
    }
}
