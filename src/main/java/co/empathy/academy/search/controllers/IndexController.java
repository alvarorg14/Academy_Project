package co.empathy.academy.search.controllers;

import co.empathy.academy.search.exceptions.BulkIndexException;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.services.IndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/index")
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * PUT /index/create - Creates a new index
     *
     * @return ResponseEntity - 200 if the index was created, 400 if the index already exists and 500 if there was an error
     */
    @Operation(summary = "Creates a new index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Index created successfully"),
            @ApiResponse(responseCode = "500", description = "Error creating the index"),
    })
    @PutMapping("/create")
    public ResponseEntity<String> createIndex() {
        try {
            indexService.createIndex();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error creating index");
        }

        return ResponseEntity.ok("Index created");
    }


    /**
     * POST /index - Indexes a new document
     *
     * @param movie - movie to be indexed
     * @return ResponseEntity - 200 if the document was indexed and 500 if there was an error
     */
    @Operation(summary = "Indexes a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document indexed successfully"),
            @ApiResponse(responseCode = "500", description = "Error indexing the document"),
    })
    @PostMapping("/{indexName}")
    public ResponseEntity<Movie> indexDocument(@RequestBody Movie movie) {
        try {
            indexService.indexDocument(movie);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.created(null).body(movie);
    }

    /**
     * POST /index/imdb - Indexes imdb data in the index
     *
     * @param basicsFile  - Title basics file containing the data to be indexed
     * @param ratingsFile - Ratings file containing the data to be indexed
     * @oaram akasFile - Akas file containing the data to be indexed
     */
    @PostMapping("/imdb")
    @Operation(summary = "Indexes imdb data in the index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data accepted for indexing"),
            @ApiResponse(responseCode = "400", description = "Error indexing the data"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity indexImdbData(@RequestParam("basics") MultipartFile basicsFile,
                                        @RequestParam("ratings") MultipartFile ratingsFile,
                                        @RequestParam("akas") MultipartFile akasFile,
                                        @RequestParam("crew") MultipartFile crewFile,
                                        @RequestParam("principals") MultipartFile principalsFile) {
        try {
            indexService.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);
        } catch (BulkIndexException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.accepted().build();
    }

}
