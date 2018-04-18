package org.anderes.cookbook.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.domain.Recipe;
import org.anderes.cookbook.domain.RecipeRepository;
import org.anderes.edu.dbunitburner.DbUnitRule;
import org.anderes.edu.dbunitburner.DbUnitRule.CleanupUsingScript;
import org.anderes.edu.dbunitburner.DbUnitRule.ShouldMatchDataSet;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSet;
import org.anderes.spring.configuration.AppConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@CleanupUsingScript(value = { "/sql/DeleteTableContentScript.sql" })
@UsingDataSet(value = { "/data/prepare.json" })
public class RecipeRepositoryIT {

    @Inject
    private RecipeRepository repository;
    
    @Inject @Rule 
    public DbUnitRule dbUnitRule;
    
    @Before
    public void before() {
        // nothing to do ...
    }
        
    @Test
    @ShouldMatchDataSet(
            value = { "/data/prepare.json" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.UUID" })
    public void shouldBeFindAll() {
        Iterable<Recipe> recipes = repository.findAll();
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.iterator().hasNext(), is(true));
        int counter = 0;
        for (Recipe recipe : recipes) {
            assertThat(recipe.getTitle(), is(notNullValue()));
            counter++;
        }
        assertThat(counter, is(4));
    }
    
    @Test
    public void shouldBeOneRecipe() {
        final Optional<Recipe> recipe = repository.findById("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        assertNotNull(recipe);
        assertThat(recipe.isPresent(), is(true));
        assertThat(recipe.get().getTitle(), is("Arabische Spaghetti"));
    }
    
    @Test
    @ShouldMatchDataSet(
            value = { "/data/prepare.json" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.UUID" })
    public void getRecipesByTitle() {
        final Collection<Recipe> recipes = repository.findByTitleLike("%Spaghetti%");

        assertNotNull(recipes);
        assertThat(recipes.size(), is(1));
        final Recipe recipe = recipes.iterator().next();
        assertThat(recipe.getTitle(), is("Arabische Spaghetti"));
    }
    
    @ShouldMatchDataSet(value = { "/data/expected-afterRecipeInsertNew.json" },
            excludeColumns = { "INGREDIENT.UUID", "INGREDIENT.RECIPE_ID", "RECIPE.UUID", "TAGS.RECIPE_ID" },
            orderBy = { "INGREDIENT.DESCRIPTION", "INGREDIENT.ANNOTATION", "RECIPE.TITLE", "TAGS.TAGS" }
    )
    @Test
    public void shouldBeSaveNewRecipe() {
        // given
        final Recipe newRecipe = RecipeBuilder.buildRecipe();
        
        // when
        final Recipe savedRecipe = repository.save(newRecipe);
        
        // then
        assertThat(savedRecipe, is(not(nullValue())));
        assertThat(savedRecipe.getUuid(), is(not(nullValue())));
        
        final Optional<Recipe> findRecipe = repository.findById(savedRecipe.getUuid());
        assertThat(findRecipe.isPresent(), is(true));
        assertNotSame(newRecipe, findRecipe.get());
        assertThat(newRecipe, is(findRecipe.get()));
    }
    
    @Test
    @ShouldMatchDataSet(value = { "/data/expected-afterRecipeUpdate.json" },
            excludeColumns = { "INGREDIENT.UUID" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.DESCRIPTION", "INGREDIENT.ANNOTATION" }
    )
    public void shouldBeUpdateRecipe() {
        final Recipe updateRecipe = repository.findById("c0e5582e-252f-4e94-8a49-e12b4b047afb").get();
        updateRecipe.setPreamble("Neuer Preamble vom Test");
        updateRecipe.addIngredient(new Ingredient("1", "Tomate", "vollreif"));
        final Recipe savedRecipe = repository.save(updateRecipe);
        
        assertThat(savedRecipe, is(not(nullValue())));
        assertThat(savedRecipe.getPreamble(), is("Neuer Preamble vom Test"));
        assertThat(savedRecipe.getIngredients().size(), is(4));
        
        final Optional<Recipe> findRecipe = repository.findById(savedRecipe.getUuid());
        
        assertThat(findRecipe, is(not(nullValue())));
        assertThat(findRecipe.isPresent(), is(true));
        assertThat(findRecipe.get().getPreamble(), is("Neuer Preamble vom Test"));
        assertThat(findRecipe.get().getIngredients().size(), is(4));
        assertNotSame(updateRecipe, findRecipe.get());
        assertThat(updateRecipe, is(findRecipe.get()));
    }
    
    @Test
    @ShouldMatchDataSet(value = { "/data/expected-afterRecipeDelete.json" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.UUID" })
    public void shouldBeDelete() {
        final Recipe toDelete = repository.findById("c0e5582e-252f-4e94-8a49-e12b4b047afb").get();
        assertThat("Das Rezept mit der ID 'c0e5582e-252f-4e94-8a49-e12b4b047afb' existiert nicht in der Datenbank", toDelete, is(not(nullValue())));
        repository.delete(toDelete);
        
        final Collection<Recipe> recipes = repository.findByTitleLike("%Spaghetti%");
        assertNotNull(recipes);
        assertThat(recipes.size(), is(0));
    }
    
    @Test
    public void shouldBeFindAllTag() {
        final List<String> tags = repository.findAllTag();
        assertThat(tags, is(notNullValue()));
        assertThat(tags.size(), is(7));
    }
}
