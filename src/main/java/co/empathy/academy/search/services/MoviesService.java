package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.repositories.MoviesConector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoviesService{

    @Autowired
    private MoviesConector moviesConector;

    public void createMovie(Movie movie){
        moviesConector.indexMovie(movie);
    }

    public List<Movie> findByTitle(String title){
        return moviesConector.findMovieByTitle(title);
    }
}
