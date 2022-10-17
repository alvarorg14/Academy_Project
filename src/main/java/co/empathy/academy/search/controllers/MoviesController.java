package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.MoviesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class MoviesController {

    @Autowired
    private MoviesService moviesService;

    /**
     * Index a movie
     * @param movie
     * @return
     */
    @PostMapping(value = "/create")
    public String createMovie(@RequestBody Movie movie){
        moviesService.createMovie(movie);
        log.info("Pelicula creada correctamente: " + movie.toString());
        return "Pelicula creada correctamente";
    }

    /**
     * Find movies by title
     * @param title
     * @return movies
     */
    @GetMapping(value = "/findByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> findByTitle(@RequestParam String title){
        List<Movie> movies = moviesService.findByTitle(title);
        log.info("Returned movie with ID: " + movies.get(0).getId());
        return movies;
    }

    /**
     * Find movies by year
     * @param year
     * @return movies
     */
    @GetMapping(value = "/findByYear/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> findByYear(@PathVariable int year) {
        List<Movie> movies = moviesService.findByYear(year);
        log.info("Returned " + movies.size() + " movies");
        return movies;
    }

    /**
     * Find movies by gender and year
     * @param gender
     * @param year
     * @return movies
     */
    @GetMapping(value = "/findByGenderYear", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> findByGenderAndYear(@RequestParam String gender, @RequestParam int year){
        List<Movie> movies = moviesService.findByGenderAndYear(gender, year);
        log.info("Returned " + movies.size() + " movies");
        return movies;
    }

}
