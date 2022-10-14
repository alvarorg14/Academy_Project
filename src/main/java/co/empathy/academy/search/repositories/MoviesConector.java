package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
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

    public List<Movie> findMovieByTitle(String title){
        try {
            SearchResponse<Movie> response = client.search(req->
                            req.index(index)
                                    .query(query->
                                            query.match(t -> t.field("title").query(title))), Movie.class);
            List<Hit<Movie>> hits = response.hits().hits();
            List<Movie> result = new ArrayList<>();
            for (Hit<Movie> hit: hits){
                result.add(hit.source());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

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
}
