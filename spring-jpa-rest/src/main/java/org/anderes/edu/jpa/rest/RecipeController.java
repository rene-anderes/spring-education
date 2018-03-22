package org.anderes.edu.jpa.rest;

import static java.lang.Boolean.TRUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("recipes")
public class RecipeController {

    @Inject 
    private RecipeRepository repository;
    
    @GetMapping(produces = { APPLICATION_JSON_VALUE })
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

    @GetMapping(value = "{id}", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<RecipeResource> showOneRecipe(@PathVariable("id") String recipeId) {
        final Optional<Recipe> recipe = repository.findById(recipeId);
        if (!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final Recipe findRecipe = recipe.get(); 
        final RecipeResource recipeResource = new RecipeResource(findRecipe.getUuid());
        DtoMapper.map(findRecipe, recipeResource);
        recipeResource.setAddingDate(findRecipe.getAddingDateTime()).setEditingDate(findRecipe.getLastUpdateTime());
        
        final Link linkRel = linkTo(RecipeController.class)
                        .slash(findRecipe.getUuid())
                        .slash("ingredients")
                        .withRel("ingredients");
        
        recipeResource.add(linkRel);
        return ResponseEntity.ok(recipeResource);
    }
    
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") String resourceId) {
        repository.deleteById(resourceId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping(consumes = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveRecipe(@Valid @RequestBody RecipeResource newResource) {
        final Recipe newRecipe = new Recipe();
        return saveNewRecipe(newResource, newRecipe, TRUE);
    }
    
    @PutMapping(value = "{id}", consumes = { APPLICATION_JSON_VALUE } )
    public ResponseEntity<?> updateRecipe(@PathVariable("id") String resourceId, @Valid @RequestBody RecipeResource resource,
            @RequestParam(value = "updateDate", required = false, defaultValue = "true") Boolean updateDate) {

        final Optional<Recipe> existsRecipe = repository.findById(resourceId);
        if (!existsRecipe.isPresent()) {
            final Recipe newRecipe = new Recipe(resourceId);
            return saveNewRecipe(resource, newRecipe, updateDate);
        } 
        DtoMapper.map(resource, existsRecipe.get());
        existsRecipe.get().setLastUpdate(LocalDateTime.now());
        repository.save(existsRecipe.get());
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> saveNewRecipe(final RecipeResource resource, final Recipe newRecipe, final Boolean updateDate) {
        DtoMapper.map(resource, newRecipe);
        if (updateDate) {
            newRecipe.setAddingDate(LocalDateTime.now());
            newRecipe.setLastUpdate(LocalDateTime.now());
        } else {
            // Datum übernehmen
            newRecipe.setAddingDate(longToLocatDateTime(resource.getAddingDate()));
            newRecipe.setLastUpdate(longToLocatDateTime(resource.getEditingDate()));
        }
        final Recipe result = repository.save(newRecipe);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getUuid()).toUri();
        return ResponseEntity.created(location).build();
    }

    private LocalDateTime longToLocatDateTime(final Long datetime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(datetime), ZoneId.systemDefault());
    }
    
    @GetMapping(value = "{id}/ingredients", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<List<IngredientResource>> showIngredients(@PathVariable("id") String recipeId) {
        final Optional<Recipe> findRecipe = repository.findById(recipeId);
        if (!findRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final List<IngredientResource> resources = findRecipe.get().getIngredients().stream()
                        .map(i -> new IngredientResource(i.getUuid(), i.getQuantity(), i.getDescription(), i.getAnnotation()))
                        .collect(Collectors.toList());
        resources.stream().forEach(r -> {
            final Link linkSelfRel = linkTo(RecipeController.class)
                            .slash(findRecipe.get().getUuid()).slash("ingredients").slash(r.getResourceId()).withSelfRel();
            r.add(linkSelfRel);
        });
        return ResponseEntity.ok().body(resources);
    }
    
    @GetMapping(value = "{id}/ingredients/{ingredientId}", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<IngredientResource> showOneIngredient(@PathVariable("id") String recipeId, @PathVariable("ingredientId") String ingredientId) {
        final Optional<Recipe> findRecipe = repository.findById(recipeId);
        if (!findRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final Optional<IngredientResource> resource = findRecipe.get().getIngredients().stream()
                        .filter(i -> i.getUuid().equals(ingredientId))
                        .map(i -> new IngredientResource(i.getUuid(), i.getQuantity(), i.getDescription(), i.getAnnotation()))
                        .findFirst();
        if (resource.isPresent()) {
            return ResponseEntity.ok().body(resource.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping(value = "{id}/ingredients/{ingredientId}", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateIngredient(@PathVariable("id") String recipeId, 
                    @PathVariable("ingredientId") String ingredientId, @Valid @RequestBody IngredientResource resource) {
        
        final Optional<Recipe> findRecipe = repository.findById(recipeId);
        if (!findRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        final Optional<Ingredient> ingredient = findRecipe.get().getIngredients().stream()
                        .filter(i -> i.getUuid().equals(ingredientId))
                        .findFirst();
        if (ingredient.isPresent()) {
            ingredient.get().setAnnotation(resource.getComment());
            ingredient.get().setDescription(resource.getDescription());
            ingredient.get().setQuantity(resource.getPortion());
            repository.save(findRecipe.get());
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping(value = "{id}/ingredients", consumes = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveIngredient(@PathVariable("id") String recipeId, @Valid @RequestBody IngredientResource resource) {
        
        final Optional<Recipe> recipe = repository.findById(recipeId);
        if (!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final Ingredient ingredient = new Ingredient(resource.getPortion(), resource.getDescription(), resource.getComment());
        recipe.get().addIngredient(ingredient);
        repository.save(recipe.get());
        
        final URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{ingredientId}")
                        .buildAndExpand(ingredient.getUuid()).toUri();
        return ResponseEntity.created(location).build();
    }
    
    @DeleteMapping(value = "{id}/ingredients/{ingredientId}")
    public ResponseEntity<?> deleteIngredient(@PathVariable("id") String recipeId, @PathVariable("ingredientId") String ingredientId) {
        
        final Optional<Recipe> recipe = repository.findById(recipeId);
        if (!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final Optional<Ingredient> ingredient = recipe.get().getIngredients().stream()
                        .filter(i -> i.getUuid().equals(ingredientId))
                        .findFirst();
        if (ingredient.isPresent()) {
            recipe.get().removeIngredient(ingredient.get());
            repository.save(recipe.get());
        }
        return ResponseEntity.ok().build();
    }
    
    @GetMapping(value = "tags", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<Collection<String>> showAllTags() {
        final List<String> tags = repository.findAllTag();
        final Set<String> filteredTags = tags.stream().filter(t -> t != null).collect(Collectors.toSet());
        return ResponseEntity.ok().body(filteredTags);
    }
        
}
