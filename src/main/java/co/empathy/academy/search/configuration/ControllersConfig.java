package co.empathy.academy.search.configuration;

import co.empathy.academy.search.controllers.QueriesController;
import co.empathy.academy.search.services.QueriesService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllersConfig {

    public QueriesController getQueriesController(QueriesService queriesService) {
        return new QueriesController(queriesService);
    }
}
