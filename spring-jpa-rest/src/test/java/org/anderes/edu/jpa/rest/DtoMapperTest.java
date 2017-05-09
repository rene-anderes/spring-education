package org.anderes.edu.jpa.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.anderes.edu.jpa.domain.Ingredient;
import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.junit.Test;

public class DtoMapperTest {
    
    @Test
    public void shouldBeMapRecipe() {
        // given
        RecipeResource resource = new RecipeResource(UUID.randomUUID().toString());
        resource.setTitle("Arabische Spaghetti").setPreamble("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird...")
            .setNoOfPerson("2").setPreparation("Pouletfleisch in schmale Streifen schneiden und kurz anbraten")
            .setRating(4).addTag("test").addTag("vegetarisch");
        final Recipe recipe = new Recipe(resource.getUuid());
        recipe.addTag("pasta");
        recipe.addIngredient(new Ingredient("100g", "Mehl", "Bioqualität"));
        recipe.addIngredient(new Ingredient("2", "Tomaten", "Bioqualität"));
        
        // when
        DtoMapper.map(resource, recipe);
        
        // then
        assertThat(recipe.getTitle(), is("Arabische Spaghetti"));
        assertThat(recipe.getTags().size(), is(2));
        assertThat(recipe.getTags(), hasItems("test", "vegetarisch"));
        assertThat(recipe.getPreamble(), is("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird..."));
        assertThat(recipe.getNoOfPerson(), is("2"));
        assertThat(recipe.getVersion(), is(nullValue()));
        assertThat(recipe.getRating(), is(4));
        assertThat(recipe.getPreparation(), is("Pouletfleisch in schmale Streifen schneiden und kurz anbraten"));
        assertThat(recipe.getIngredients().size(), is(2));
        
    }

}
