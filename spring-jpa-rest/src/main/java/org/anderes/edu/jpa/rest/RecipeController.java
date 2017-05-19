package org.anderes.edu.jpa.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.anderes.edu.jpa.domain.Ingredient;
import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.domain.RecipeRepository;
import org.anderes.edu.jpa.rest.dto.IngredientResource;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.anderes.edu.jpa.rest.dto.RecipeShortResource;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("recipes")
public class RecipeController {

    @Inject 
    private RecipeRepository repository;
    
    @RequestMapping(method = GET, produces = { APPLICATION_JSON_VALUE })
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

    @RequestMapping(method = GET, value = "{id}", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<RecipeResource> showOneRecipe(@PathVariable("id") String recipeId) {
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        final RecipeResource recipeResource = new RecipeResource(findRecipe.getUuid());
        recipeResource.setTitle(findRecipe.getTitle()).setPreamble(findRecipe.getPreamble())
            .setNoOfPerson(findRecipe.getNoOfPerson()).setPreparation(findRecipe.getPreparation())
            .setAddingDate(findRecipe.getAddingDateTime()).setEditingDate(findRecipe.getLastUpdateTime())
            .setRating(findRecipe.getRating());
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
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") String resourceId) {
        repository.delete(resourceId);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = POST, consumes = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveRecipe(@RequestBody RecipeResource newResource) {
        final Recipe recipe = new Recipe();
        DtoMapper.map(newResource, recipe);
        final Recipe result = repository.save(recipe);
        final URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{id}")
                        .buildAndExpand(result.getUuid()).toUri();

        return ResponseEntity.created(location).build();
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = PUT, value = "{id}", consumes = { APPLICATION_JSON_VALUE } )
    public ResponseEntity<?> updateRecipe(@PathVariable("id") String resourceId, @RequestBody RecipeResource resource) {
        final Recipe findRecipe = repository.findOne(resourceId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        DtoMapper.map(resource, findRecipe);
        repository.save(findRecipe);
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(method = GET, value = "{id}/ingredients", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<List<IngredientResource>> showIngredients(@PathVariable("id") String recipeId) {
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        final List<IngredientResource> resources = findRecipe.getIngredients().stream()
                        .map(i -> new IngredientResource(i.getUuid(), i.getQuantity(), i.getDescription(), i.getAnnotation()))
                        .collect(Collectors.toList());
        resources.stream().forEach(r -> {
            final Link linkSelfRel = linkTo(RecipeController.class)
                            .slash(findRecipe.getUuid()).slash("ingredients").slash(r.getResourceId()).withSelfRel();
            r.add(linkSelfRel);
        });
        return ResponseEntity.ok().body(resources);
    }
    
    @RequestMapping(method = GET, value = "{id}/ingredients/{ingredientId}", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<IngredientResource> showOneIngredient(@PathVariable("id") String recipeId, @PathVariable("ingredientId") String ingredientId) {
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        final Optional<IngredientResource> resource = findRecipe.getIngredients().stream()
                        .filter(i -> i.getUuid().equals(ingredientId))
                        .map(i -> new IngredientResource(i.getUuid(), i.getQuantity(), i.getDescription(), i.getAnnotation()))
                        .findFirst();
        if (resource.isPresent()) {
            return ResponseEntity.ok().body(resource.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @RequestMapping(method = PUT, value = "{id}/ingredients/{ingredientId}", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateIngredient(@PathVariable("id") String recipeId, 
                    @PathVariable("ingredientId") String ingredientId, @RequestBody IngredientResource resource) {
        
        final Recipe findRecipe = repository.findOne(recipeId);
        if (findRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        
        final Optional<Ingredient> ingredient = findRecipe.getIngredients().stream().filter(i -> i.getUuid().equals(ingredientId)).findFirst();
        if (ingredient.isPresent()) {
            ingredient.get().setAnnotation(resource.getComment());
            ingredient.get().setDescription(resource.getDescription());
            ingredient.get().setQuantity(resource.getPortion());
            repository.save(findRecipe);
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = POST, value = "{id}/ingredients", consumes = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveIngredient(@PathVariable("id") String recipeId, @RequestBody IngredientResource resource) {
        
        final Recipe recipe = repository.findOne(recipeId);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        final Ingredient ingredient = new Ingredient(resource.getPortion(), resource.getDescription(), resource.getComment());
        recipe.addIngredient(ingredient);
        repository.save(recipe);
        
        final URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{ingredientId}")
                        .buildAndExpand(ingredient.getUuid()).toUri();
        return ResponseEntity.created(location).build();
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = DELETE, value = "{id}/ingredients/{ingredientId}")
    public ResponseEntity<?> deleteIngredient(@PathVariable("id") String recipeId, @PathVariable("ingredientId") String ingredientId) {
        
        final Recipe recipe = repository.findOne(recipeId);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        final Optional<Ingredient> ingredient = recipe.getIngredients().stream().filter(i -> i.getUuid().equals(ingredientId)).findFirst();
        if (ingredient.isPresent()) {
            recipe.removeIngredient(ingredient.get());
            repository.save(recipe);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @RequestMapping(method = GET, value = "tags", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<Collection<String>> showAllTags() {
        final List<String> tags = repository.findAllTag();
        return ResponseEntity.ok().body(new HashSet<String>(tags));
    }
        
}
