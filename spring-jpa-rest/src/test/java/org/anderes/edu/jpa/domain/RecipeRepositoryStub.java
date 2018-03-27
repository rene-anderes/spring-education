package org.anderes.edu.jpa.domain;

import java.time.LocalDateTime;
import static java.time.Month.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RecipeRepositoryStub implements RecipeRepository {

    private final Map<String, Recipe> recipes = new HashMap<>();

    public RecipeRepositoryStub() {
        super();
        initialize();
    }

    @Override
    public List<Recipe> findAll() {
        final List<Recipe> list = recipes.values().stream()
                        .sorted((r1, r2) -> r1.getTitle().compareToIgnoreCase(r2.getTitle()))
                        .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Recipe> findAll(Sort sort) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public List<Recipe> findAllById(Iterable<String> ids) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public <S extends Recipe> List<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public void flush() {
        // nothing to do 
    }

    @Override
    public <S extends Recipe> S saveAndFlush(S entity) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public void deleteInBatch(Iterable<Recipe> entities) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public void deleteAllInBatch() {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public Recipe getOne(String id) {
        return recipes.get(id);
    }

    @Override
    public <S extends Recipe> List<S> findAll(Example<S> example) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public <S extends Recipe> List<S> findAll(Example<S> example, Sort sort) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public Page<Recipe> findAll(Pageable page) {
        return new PageImpl<Recipe>(findAll());
    }

    @Override
    public long count() {
        return recipes.size();
    }

    @Override
    public void delete(Recipe recipe) {
        recipes.remove(recipe.getUuid());
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public void deleteAll(Iterable<? extends Recipe> arg0) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public void deleteById(String id) {
        recipes.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return recipes.containsKey(id);
    }

    @Override
    public Optional<Recipe> findById(String id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends Recipe> S save(S recipe) {
        recipes.put(recipe.getUuid(), recipe);
        return (S) recipes.get(recipe.getUuid());
    }

    @Override
    public <S extends Recipe> long count(Example<S> arg0) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public <S extends Recipe> boolean exists(Example<S> arg0) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public <S extends Recipe> Page<S> findAll(Example<S> arg0, Pageable arg1) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public <S extends Recipe> Optional<S> findOne(Example<S> arg0) {
        throw new NotImplementedException("stub for testing");
    }

    @Override
    public Collection<Recipe> findByTitleLike(String title) {
        return recipes.values().stream().filter(r -> r.getTitle().equals(title)).collect(Collectors.toList());
    }

    @Override
    public List<String> findAllTag() {
        final HashSet<String> tags = new HashSet<>();
        recipes.values().stream().forEach(r -> r.getTags().stream().forEach(t -> tags.add(t)));
        return new ArrayList<>(tags);
    }
    
    public void initialize() {
        recipes.clear();
        final Recipe recipe1 = createStubRecipe1();
        recipes.put(recipe1.getUuid(), recipe1);
        final Recipe recipe2 = createStubRecipe2();
        recipes.put(recipe2.getUuid(), recipe2);
        final Recipe recipe3 = createStubRecipe3();
        recipes.put(recipe3.getUuid(), recipe3);
        final Recipe recipe4 = createStubRecipe4();
        recipes.put(recipe4.getUuid(), recipe4);
    }

    private Recipe createStubRecipe1() {
        final Recipe recipe = new Recipe("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        recipe.setTitle("Arabische Spaghetti")
            .setAddingDate(LocalDateTime.of(2014, JANUARY, 22, 23, 3, 20))
            .setLastUpdate(LocalDateTime.of(2014, JANUARY, 22, 23, 3, 20))
            .setPreamble("Da bei diesem Rezept das Scharfe (Curry)...")
            .setNoOfPerson("2").setPreparation("Pouletfleisch in schmale Streifen schneiden und ...")
            .setRating(4).addTag("pasta").addTag("fleisch")
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047112", "250g", "Spaghetti", null))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047113", "200-300g", "Poulet", "Bruststücke"))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047114", "4-5", "Frühlingszwiebeln", "oder Lauch"));
        return recipe;
    }
    
    private Recipe createStubRecipe2() {
        final Recipe recipe = new Recipe("adf99b55-4804-4398-af4e-e37ec2c692c7");
        recipe.setTitle("Avocadotatar")
            .setAddingDate(LocalDateTime.of(2014, JANUARY, 23, 23, 3, 20))
            .setLastUpdate(LocalDateTime.of(2015, JANUARY, 31, 23, 3, 20))
            .setNoOfPerson("4").setPreparation("Die Zitrone auspressen und den Saft, mit dem Öl ...")
            .setRating(5).addTag("kalt").addTag("vorspeise")
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047111", "50g", "Zucker", null))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047115", "2-3", "Peproni", "gelb oder rot"))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047116", "1", "Tomate", null))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047117", "1", "Peperoncini", null));
        return recipe;
    }
    
    private Recipe createStubRecipe3() {
        final Recipe recipe = new Recipe("adf99b55-4804-4398-af55-e37ec2c692ff");
        recipe.setTitle("Arabische Spaghetti")
            .setAddingDate(LocalDateTime.of(2016, JANUARY, 23, 23, 3, 20))
            .setLastUpdate(LocalDateTime.of(2014, JANUARY, 31, 23, 3, 20))
            .setPreamble("Da bei diesem Rezept das Scharfe (Curry)...")
            .setNoOfPerson("4-5").setPreparation("Die Auberginen in ca. 4-5mm dicke Streifen schneiden ...")
            .setRating(5).addTag("pasta").addTag("vorspeise")
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047211", "1 kg", "Auberginen", null))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047212", "100g", "Feta-Käse", null))
            .addIngredient(new Ingredient("c0e5582e-252f-4e94-8a49-e12b4b047213", "2-3", "Knoblizehen", "Bio"));
        return recipe;
    }
    
    private Recipe createStubRecipe4() {
        final Recipe recipe = new Recipe("aaa99b55-4804-4398-af55-e37ec2c692ff");
        recipe.setTitle("to be done")
            .setAddingDate(LocalDateTime.of(2016, JANUARY, 23, 23, 3, 20))
            .setLastUpdate(LocalDateTime.of(2014, JANUARY, 31, 23, 3, 20))
            .setPreamble("Da bei diesem Rezept das Scharfe (Curry)...")
            .setNoOfPerson("1").setPreparation("to be done ...")
            .setRating(1);
        return recipe;
    }
}
