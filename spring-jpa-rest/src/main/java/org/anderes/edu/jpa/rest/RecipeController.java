package org.anderes.edu.jpa.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.domain.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping("recipes")
public class RecipeController {

    @Inject 
    private RecipeRepository repository;
    
    @RequestMapping(method = GET, produces={APPLICATION_JSON_VALUE})
    public Page<RecipeShortResource> showRecipeShort(Pageable pageable) {
        final Page<Recipe> collection = repository.findAll(pageable);
        
        final List<RecipeShortResource> content = collection.getContent().stream()
                        .map(r -> new RecipeShortResource(r.getUuid(), r.getTitle()))
                        .collect(Collectors.toList());
        
        content.stream().forEach(r -> {
            final Link linkSelfRel = linkTo(RecipeController.class).slash(r.getUuid()).withSelfRel();
            r.add(linkSelfRel);
        });
        return new PageImpl<RecipeShortResource>(content, pageable, content.size());
    }

    @RequestMapping(method = GET, value = "{id}")
    public HttpEntity<RecipeResource> showOneRecipe(@PathVariable("id") String recipeId) {
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        final RecipeResource recipeResource = new RecipeResource(findRecipe.getUuid());
        recipeResource.setTitle(findRecipe.getTitle()).setPreamble(findRecipe.getPreamble())
            .setNoOfPerson(findRecipe.getNoOfPerson()).setPreparation(findRecipe.getPreparation())
            .setAddingDate(findRecipe.getAddingDate()).setLastUpdate(findRecipe.getLastUpdate())
            .setRating(findRecipe.getRating()).setVersion(findRecipe.getVersion());
        findRecipe.getTags().stream().forEach(t -> recipeResource.addTag(t));
        
        final Link linkRel = linkTo(RecipeController.class)
                        .slash(findRecipe.getUuid())
                        .slash("ingredients")
                        .withRel("ingredients");
        
        recipeResource.add(linkRel);
        return new HttpEntity<RecipeResource>(recipeResource);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
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
    
    @RequestMapping(method = GET, value = "{id}/ingredients")
    public HttpEntity<List<IngredientResource>> showIngredients(@PathVariable("id") String recipeId) {
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        final List<IngredientResource> resources = findRecipe.getIngredients().stream()
                        .map(i -> new IngredientResource(i.getId(), i.getQuantity(), i.getDescription(), i.getAnnotation()))
                        .collect(Collectors.toList());
        resources.stream().forEach(r -> {
            final Link linkSelfRel = linkTo(RecipeController.class)
                            .slash(findRecipe.getUuid()).slash("ingredients").slash(r.getDbId()).withSelfRel();
            r.add(linkSelfRel);
        });
        return ResponseEntity.ok().body(resources);
    }
    
    @RequestMapping(method = GET, value = "{id}/ingredients/{imageId}")
    public HttpEntity<IngredientResource> showOneIngredient(@PathVariable("id") String recipeId, @PathVariable("imageId") Long imageId) {
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        final Optional<IngredientResource> resource = findRecipe.getIngredients().stream()
                        .filter(i -> i.getId() == imageId)
                        .map(i -> new IngredientResource(i.getId(), i.getQuantity(), i.getDescription(), i.getAnnotation()))
                        .findFirst();
        if (resource.isPresent()) {
            return ResponseEntity.ok().body(resource.get());
        }
        return ResponseEntity.notFound().build();
    }
}
