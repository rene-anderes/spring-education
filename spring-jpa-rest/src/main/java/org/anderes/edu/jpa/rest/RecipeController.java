package org.anderes.edu.jpa.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;

import javax.inject.Inject;

import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.domain.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("recipes")
public class RecipeController {

    @Inject 
    private RecipeRepository repository;

    @RequestMapping(method = GET, produces={APPLICATION_JSON_VALUE})
    public Page<Recipe> showRecipes(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @RequestMapping(method = GET, value = "{id}")
    public HttpEntity<Recipe> showRecipe(@PathVariable("id") Recipe recipe) {
        return new HttpEntity<Recipe>(repository.findOne(recipe.getUuid()));
    }
    
    @RequestMapping(method = DELETE, value = "{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") Recipe recipe) {
        repository.delete(recipe.getUuid());
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = POST, consumes={APPLICATION_JSON_VALUE}, produces={APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveRecipe(@RequestBody Recipe recipe) {
        
        final Recipe result = repository.save(recipe);
        final URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{id}")
                        .buildAndExpand(result.getUuid()).toUri();

        return ResponseEntity.created(location).build();
    }
}
