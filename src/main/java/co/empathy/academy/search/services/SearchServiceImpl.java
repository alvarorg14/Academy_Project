package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.QueryResponse;
import co.empathy.academy.search.models.facets.Facet;
import co.empathy.academy.search.repositories.ElasticEngine;
import co.empathy.academy.search.repositories.ElasticLowClient;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchServiceImpl implements SearchService {

    private final ElasticLowClient elasticLowClient;

    private final ElasticEngine elasticEngine;

    private final QueriesService queriesService;

    public SearchServiceImpl(ElasticLowClient elasticLowClient, ElasticEngine elasticEngine, QueriesService queriesService) {
        this.elasticLowClient = elasticLowClient;
        this.elasticEngine = elasticEngine;
        this.queriesService = queriesService;
    }

    /*
    @Override
    public void makeAggsQuery() throws IOException {
        elasticEngine.makeAggsQuery();
    }*/

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
        return elasticEngine.performQuery(queriesService.multiMatch(query, fieldsArray), 100, new ArrayList<>());
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
        return elasticEngine.performQuery(queriesService.termQuery(value, field), 100, new ArrayList<>());
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
        return elasticEngine.performQuery(queriesService.termsQuery(valuesArray, field), 100, new ArrayList<>());
    }

    /**
     * Performs a search with all the filters
     *
     * @param genres     Genres to search
     * @param types      Types to search
     * @param maxYear    Maximum year to search
     * @param minYear    Minimum year to search
     * @param maxRuntime Maximum runtime minutes
     * @param minRuntime Minimum runtime minutes
     * @param maxScore   Maximum average rating
     * @param minScore   Minimum average rating
     * @param maxNHits   Maximum number of hits
     * @param sortRating Sort by rating
     * @return List of movies that match the filters
     * @throws IOException
     */
    @Override
    public List<Movie> allFiltersSearch(Optional<String> genres, Optional<String> types,
                                        Optional<Integer> maxYear, Optional<Integer> minYear,
                                        Optional<Integer> maxRuntime, Optional<Integer> minRuntime,
                                        Optional<Double> maxScore, Optional<Double> minScore,
                                        Optional<Integer> maxNHits, Optional<String> sortRating) throws IOException {

        List<Query> filters = new ArrayList<>();

        if (genres.isPresent()) {
            String[] genresArray = genres.get().split(",");
            List<Query> genreQueries = queriesService.termQueries(genresArray, "genres");
            filters.add(queriesService.shouldQuery(genreQueries));
        }

        if (types.isPresent()) {
            String[] typesArray = types.get().split(",");
            List<Query> typeQueries = queriesService.termQueries(typesArray, "titleType");
            filters.add(queriesService.shouldQuery(typeQueries));
        }

        if (maxYear.isPresent() || minYear.isPresent()) {
            filters.add(queriesService.rangeIntegerQuery("startYear", minYear.isPresent() ? minYear.get() : 0,
                    maxYear.isPresent() ? maxYear.get() : Integer.MAX_VALUE));
        }

        if (maxRuntime.isPresent() || minRuntime.isPresent()) {
            filters.add(queriesService.rangeIntegerQuery("runtimeMinutes", minRuntime.isPresent() ? minRuntime.get() : 0,
                    maxRuntime.isPresent() ? maxRuntime.get() : Integer.MAX_VALUE));
        }

        if (maxScore.isPresent() || minScore.isPresent()) {
            filters.add(queriesService.rangeDoubleQuery("averageRating", minScore.isPresent() ? minScore.get() : 0,
                    maxScore.isPresent() ? maxScore.get() : Double.MAX_VALUE));
        }

        List<SortOptions> sortOptions = new ArrayList<>() {{
            add(queriesService.sort("averageRating", sortRating.orElse("desc")));
            add(queriesService.sort("numVotes", sortRating.orElse("desc")));
        }};
        return elasticEngine.performQuery(queriesService.boolQuery(filters), maxNHits.orElse(100), sortOptions);
    }

    /**
     * Performs a search to get the genres aggregation
     *
     * @return Facet with the genres aggregation
     * @throws IOException
     */
    @Override
    public Facet getGenresAggregation() throws IOException {
        return elasticEngine.getGenresAggregation();
    }


}
