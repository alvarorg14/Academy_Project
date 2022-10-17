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

    @PostMapping(value = "/create")
    public String createMovie(@RequestBody Movie movie){
        moviesService.createMovie(movie);
        log.info("Pelicula creada correctamente: " + movie.toString());
        return "Pelicula creada correctamente";
    }

    @GetMapping(value = "/findByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> findByTitle(@RequestParam String title){
        List<Movie> movies = moviesService.findByTitle(title);
        log.info("Returned movie with ID: " + movies.get(0).getId());
        return movies;
    }
}
