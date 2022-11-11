package co.empathy.academy.search.configuration;

import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticLowClient;
import co.empathy.academy.search.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public SearchService getSearchService(ElasticLowClient elasticLowClient, ElasticEngine elasticEngine,
                                          QueriesService queriesService) {
        return new SearchServiceImpl(elasticLowClient, elasticEngine, queriesService);
    }

    @Bean
    public IndexService getIndexService(ElasticEngine elasticEngine) {
        return new IndexServiceImpl(elasticEngine);
    }

    @Bean
    public QueriesService getQueriesService() {
        return new QueriesServiceImpl();
    }
}
