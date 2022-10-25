package co.empathy.academy.search.configuration;

import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticLowClient;
import co.empathy.academy.search.services.IndexService;
import co.empathy.academy.search.services.IndexServiceImpl;
import co.empathy.academy.search.services.QueriesService;
import co.empathy.academy.search.services.QueriesServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public QueriesService getSearchService(ElasticLowClient elasticLowClient, ElasticEngine elasticEngine) {
        return new QueriesServiceImpl(elasticLowClient, elasticEngine);
    }

    @Bean
    public IndexService getIndexService(ElasticEngine elasticEngine) {
        return new IndexServiceImpl(elasticEngine);
    }
}
