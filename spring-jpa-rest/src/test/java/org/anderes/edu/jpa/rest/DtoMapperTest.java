package org.anderes.edu.jpa.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.anderes.edu.jpa.domain.Ingredient;
import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class DtoMapperTest {
    
    @Test
    public void shouldBeMapRecipe() {
        // given
        RecipeResource resource = new RecipeResource(UUID.randomUUID().toString());
        resource.setTitle("Arabische Spaghetti").setPreamble("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird...")
            .setNoOfPerson("2").setPreparation("Pouletfleisch in schmale Streifen schneiden und kurz anbraten")
            .setRating(4).addTag("test").addTag("vegetarisch").setAddingDate(december(2000, 29)).setEditingDate(december(2001, 29));
        final Recipe recipe = new Recipe(resource.getUuid());
        recipe.addTag("pasta");
        recipe.addIngredient(new Ingredient("100g", "Mehl", "Bioqualität"));
        recipe.addIngredient(new Ingredient("2", "Tomaten", "Bioqualität"));
        
        // when
        DtoMapper.map(resource, recipe);
        
        // then
        assertThat(recipe.getUuid(), is(resource.getUuid()));
        assertThat(recipe.getTitle(), is("Arabische Spaghetti"));
        assertThat(recipe.getTags().size(), is(2));
        assertThat(recipe.getTags(), hasItems("test", "vegetarisch"));
        assertThat(recipe.getPreamble(), is("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird..."));
        assertThat(recipe.getNoOfPerson(), is("2"));
        assertThat(recipe.getVersion(), is(nullValue()));
        assertThat(recipe.getRating(), is(4));
        assertThat(recipe.getPreparation(), is("Pouletfleisch in schmale Streifen schneiden und kurz anbraten"));
        assertThat(recipe.getIngredients().size(), is(2));
        assertThat(LocalDateTime.now().minusMinutes(1l).isBefore(recipe.getAddingDate()), is(true));
        assertThat(LocalDateTime.now().minusMinutes(1l).isBefore(recipe.getLastUpdate()), is(true));
    }

    @Test
    public void shouldBeMapRecipeResource() {
    
        // given
        final Recipe recipe = new Recipe(UUID.randomUUID().toString());
        recipe.setTitle("Arabische Spaghetti").setPreamble("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird...")
            .setNoOfPerson("2").setPreparation("Pouletfleisch in schmale Streifen schneiden und kurz anbraten")
            .setRating(4).addTag("test").addTag("vegetarisch").setAddingDate(LocalDateTime.of(2000, Month.DECEMBER, 29, 0, 0))
            .setLastUpdate(LocalDateTime.of(2000, Month.DECEMBER, 29, 0, 0));
        
        final RecipeResource resource = new RecipeResource(recipe.getUuid());
        
        // when
        DtoMapper.map(recipe, resource);
        
        // then
        assertThat(resource.getUuid(), is(recipe.getUuid()));
        assertThat(resource.getTitle(), is("Arabische Spaghetti"));
        assertThat(resource.getTags().size(), is(2));
        assertThat(resource.getTags(), hasItems("test", "vegetarisch"));
        assertThat(resource.getPreamble(), is("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird..."));
        assertThat(resource.getNoOfPerson(), is("2"));
        assertThat(resource.getRating(), is(4));
        assertThat(resource.getPreparation(), is("Pouletfleisch in schmale Streifen schneiden und kurz anbraten"));
    }
    
    private Long december(int year, int dayOfMonth) {
        final LocalDate localDate = LocalDate.of(year, Month.DECEMBER, dayOfMonth);
        return DateUtils.truncate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Calendar.DAY_OF_MONTH).getTime();
    }

}
