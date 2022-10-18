package co.empathy.academy.search.configuration;

import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.services.QueriesService;
import co.empathy.academy.search.services.QueriesServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public QueriesService getSearchService(ElasticEngine elasticEngine) {
        return new QueriesServiceImpl(elasticEngine);
    }
}
