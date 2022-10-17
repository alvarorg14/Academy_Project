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

    /**
     * Create a movie
     * @param movie
     */
    public void createMovie(Movie movie){
        moviesConector.indexMovie(movie);
    }

    /**
     * Find movies by title
     * @param title
     * @return movies
     */
    public List<Movie> findByTitle(String title){
        return moviesConector.findMovieByTitle(title);
    }

    /**
     * Find movies by year
     * @param year
     * @return movies
     */
    public List<Movie> findByYear(int year) {
        return moviesConector.findMoviesByYear(year);
    }

    /**
     * Find movies by gender and year
     * @param gender
     * @param year
     * @return movies
     */
    public List<Movie> findByGenderAndYear(String gender, int year){
        return moviesConector.findMoviesByGenderAndYear(gender, year);
    }
}
