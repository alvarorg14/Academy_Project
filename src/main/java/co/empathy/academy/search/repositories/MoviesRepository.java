package co.empathy.academy.search.repositories;

import co.empathy.academy.search.entieties.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MoviesRepository extends ElasticsearchRepository<Movie, String> {

    List<Movie> findByTitle(String title);

    List<Movie> findByTitleContaining(String title);

    List<Movie> findByTitleAndGender(String title, String gender);
}
