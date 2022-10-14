package co.empathy.academy.search.services;

import co.empathy.academy.search.entieties.Movie;
import co.empathy.academy.search.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoviesService {

    @Autowired
    private MoviesRepository moviesRepository;

    public void createMovie(Movie movie){
        moviesRepository.save(movie);
    }

    public void createMovieBulk(List<Movie> movies){
        moviesRepository.saveAll(movies);
    }

    public List<Movie> findByTitle(String title){
        return moviesRepository.findByTitle(title);
    }
}
