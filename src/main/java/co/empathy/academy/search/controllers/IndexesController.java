package co.empathy.academy.search.controllers;

import co.empathy.academy.search.services.IndexesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class IndexesController {

    @Autowired
    private IndexesService indexesService;

    @RequestMapping(value = "/index/create/{name}", method = RequestMethod.POST)
    public String createIndex(@PathVariable("name") String name){
        indexesService.createIndex(name);
        return "Indice " + name + " creado correctamente.";
    }
}
