package org.anderes.cookbook.web.rest;

import static java.lang.Boolean.TRUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.domain.Recipe;
import org.anderes.cookbook.domain.RecipeRepository;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.anderes.cookbook.web.rest.dto.RecipeResource;
import org.anderes.cookbook.web.rest.dto.RecipeShortResource;
import org.springframework.core.convert.ConversionService;
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
    @Inject
    private ConversionService conversionService;
    
    @GetMapping(produces = { APPLICATION_JSON_VALUE })
    public Page<RecipeShortResource> showRecipeShort(Pageable pageable) {
        
        final Page<Recipe> page = repository.findAll(pageable);
        
        final List<RecipeShortResource> content = page.getContent().stream()
                        .map(r -> new RecipeShortResource(r.getUuid(), r.getTitle()))
                        .collect(Collectors.toList());
        
        content.stream().forEach(r -> {
            final Link linkSelfRel = linkTo(RecipeController.class).slash(r.getUuid()).withSelfRel();
            r.add(linkSelfRel);
        });
        return new PageImpl<RecipeShortResource>(content, pageable, page.getTotalElements());
    }

    @GetMapping(value = "{id}", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<RecipeResource> showOneRecipe(@PathVariable("id") String recipeId) {
        final Optional<Recipe> recipe = repository.findById(recipeId);
        if (!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final RecipeResource recipeResource = conversionService.convert(recipe.get(), RecipeResource.class);
        
        final Link linkRel = linkTo(RecipeController.class)
                        .slash(recipeResource.getUuid())
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
        final String uuid = saveNewRecipe(newResource, TRUE);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(uuid).toUri();
        return ResponseEntity.created(location).build();
    }
    
    @PutMapping(value = "{id}", consumes = { APPLICATION_JSON_VALUE } )
    public ResponseEntity<?> updateRecipe(@PathVariable("id") String resourceId, @Valid @RequestBody RecipeResource resource,
            @RequestParam(value = "updateDate", required = false, defaultValue = "true") Boolean updateDate, HttpServletRequest request) {

        if (!resourceId.equals(resource.getUuid())) {
            return ResponseEntity.badRequest().build();
        }

        final Optional<Recipe> existsRecipe = repository.findById(resourceId);
        if (existsRecipe.isPresent()) {
            // bestehendes Rezept wird aktualisiert
            DtoHelper.updateRecipe(resource, existsRecipe.get());
            repository.save(existsRecipe.get());
            return ResponseEntity.ok().build();
        } 
        
        // ein neues Rezept wird gespeichert
        saveNewRecipe(resource, updateDate);
        final URI location = ServletUriComponentsBuilder.fromRequestUri(request).build().toUri();
        return ResponseEntity.created(location).build();
    }

    private String saveNewRecipe(final RecipeResource resource, final Boolean updateDate) {
        final Recipe newRecipe = conversionService.convert(resource, Recipe.class);
        if (updateDate) {
            newRecipe.setAddingDate(LocalDateTime.now());
            newRecipe.setLastUpdate(LocalDateTime.now());
        }
        final Recipe result = repository.save(newRecipe);
        return result.getUuid();
    }
    
    @GetMapping(value = "{id}/ingredients", produces = { APPLICATION_JSON_VALUE })
    public HttpEntity<List<IngredientResource>> showIngredients(@PathVariable("id") String recipeId) {
        final Optional<Recipe> findRecipe = repository.findById(recipeId);
        if (!findRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final List<IngredientResource> resources = findRecipe.get().getIngredients().stream()
                        .map(i -> conversionService.convert(i, IngredientResource.class))
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
                        .map(i -> conversionService.convert(i, IngredientResource.class))
                        .findFirst();
        if (resource.isPresent()) {
            return ResponseEntity.ok().body(resource.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping(value = "{id}/ingredients/{ingredientId}", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateIngredient(@PathVariable("id") String recipeId, 
                    @PathVariable("ingredientId") String ingredientId, @Valid @RequestBody IngredientResource resource,
                    HttpServletRequest request) {
        
        final Optional<Recipe> findRecipe = repository.findById(recipeId);
        if (!findRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (!resource.getResourceId().equals(ingredientId)) {
            return ResponseEntity.badRequest().build();
        }
        
        final Optional<Ingredient> ingredient = findRecipe.get().getIngredients().stream()
                        .filter(i -> i.getUuid().equals(ingredientId)).findFirst();
        
        if (ingredient.isPresent()) {
            // bestehende Zutat aktualisieren
            DtoHelper.updateIngredient(resource, ingredient.get());
            repository.save(findRecipe.get());
            return ResponseEntity.ok().build();
        }
        
        // neue Zutat speichern
        saveNewIngredient(findRecipe.get(), resource);
        final URI location = ServletUriComponentsBuilder.fromRequestUri(request).build().toUri();
        return ResponseEntity.created(location).build();
    }
    
    @PostMapping(value = "{id}/ingredients", consumes = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveIngredient(@PathVariable("id") String recipeId, @Valid @RequestBody IngredientResource resource) {
        
        final Optional<Recipe> recipe = repository.findById(recipeId);
        if (!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final String uuid = saveNewIngredient(recipe.get(), resource);
        final URI location = ServletUriComponentsBuilder 
                        .fromCurrentRequest().path("/{ingredientId}")
                        .buildAndExpand(uuid).toUri();
        return ResponseEntity.created(location).build();
    }
    
    private String saveNewIngredient(final Recipe recipe, final IngredientResource resource) {
        final Ingredient ingredient = conversionService.convert(resource, Ingredient.class);
        recipe.addIngredient(ingredient);
        repository.save(recipe);
        return ingredient.getUuid();
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
        final Set<String> filteredTags = tags.stream().filter(t -> t != null).sorted().collect(Collectors.toSet());
        return ResponseEntity.ok().body(filteredTags);
    }
        
}
