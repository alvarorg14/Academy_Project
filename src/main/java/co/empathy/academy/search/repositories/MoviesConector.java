package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.entities.Movie;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoviesConector {

    @Value("movies")
    private String index;

    @Autowired
    private ElasticsearchClient client;

    /**
     * Index a movie
     * @param movie
     * @return
     * @throws IOException
     */
    public void indexMovie(Movie movie){
        IndexRequest<Movie> request = IndexRequest.of(i -> i
                .index(index)
                .id(movie.getId())
                .document(movie));

        try {
            IndexResponse response = client.index(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find movies by title
     * @param title
     * @return movies
     */
    public List<Movie> findMovieByTitle(String title){
        try {
            SearchResponse<Movie> response = client.search(req->
                    req.index(index)
                            .query(query->
                                    query.match(t -> t.field("title").query(title))), Movie.class);
            List<Hit<Movie>> hits = response.hits().hits();
            List<Movie> result = new ArrayList<>();
            for (Hit<Movie> hit: hits){
                Movie movie = hit.source();
                movie.setId(hit.id());
                result.add(movie);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Find movies by year
     * @param year
     * @return movies
     */
    public List<Movie> findMoviesByYear(int year) {
        List<Movie> result = new ArrayList<>();
        try {
            SearchResponse<Movie> response = client.search(req ->
                    req.index(index)
                            .query(query ->
                                    query.match(t -> t.field("year").query(year))), Movie.class);
            List<Hit<Movie>> hits = response.hits().hits();

            for (Hit<Movie> hit : hits) {
                Movie movie = hit.source();
                movie.setId(hit.id());
                result.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Find movies by gender and year
     * @param gender
     * @param year
     * @return movies
     */
    public List<Movie> findMoviesByGenderAndYear(String gender, int year) {
        List<Movie> result = new ArrayList<>();
        try {
            Query byGender = MatchQuery.of(t -> t
                    .field("gender")
                    .query(gender))._toQuery();
            Query byYear = MatchQuery.of(t -> t
                    .field("year")
                    .query(year))._toQuery();

            SearchResponse<Movie> response = client.search(req ->
                    req.index(index)
                            .query(query ->
                                    query.bool(b -> b
                                            .must(byGender)
                                            .must(byYear))), Movie.class);

            List<Hit<Movie>> hits = response.hits().hits();
            for (Hit<Movie> hit : hits) {
                Movie movie = hit.source();
                movie.setId(hit.id());
                result.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
