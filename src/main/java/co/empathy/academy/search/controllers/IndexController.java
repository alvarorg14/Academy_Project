package co.empathy.academy.search.controllers;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.services.IndexService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/index")
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * PUT /index/create/{name} - Creates a new index
     *
     * @param name - name of the index
     * @return ResponseEntity - 200 if the index was created, 400 if the index already exists and 500 if there was an error
     */
    @PutMapping("/create/{name}")
    public ResponseEntity<String> createIndex(@PathVariable String name) {
        try {
            indexService.createIndex(name);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error creating index");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Index already exists");
        }
        return ResponseEntity.ok("Index created");
    }

    /**
     * POST /index - Indexes a new document
     *
     * @param indexName - name of the index where the document will be indexed
     * @param movie     - movie to be indexed
     * @return ResponseEntity - 200 if the document was indexed and 500 if there was an error
     */
    @PostMapping("/{indexName}")
    public ResponseEntity<Movie> indexDocument(@PathVariable String indexName, @RequestBody Movie movie) {
        try {
            indexService.indexDocument(indexName, movie);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

}
