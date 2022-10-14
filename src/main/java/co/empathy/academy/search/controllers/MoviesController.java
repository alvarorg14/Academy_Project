package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entieties.Movie;
import co.empathy.academy.search.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MoviesController {

    @Autowired
    private MoviesService moviesService;

    @PostMapping(value = "/create")
    public String createMovie(@RequestBody Movie movie){
        moviesService.createMovie(movie);
        return "correcto";
    }

    @GetMapping(value = "/findByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> findByTitle(@RequestParam String title){
        return moviesService.findByTitle(title);
    }
}
