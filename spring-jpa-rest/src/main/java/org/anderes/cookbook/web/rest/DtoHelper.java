package org.anderes.cookbook.web.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.domain.Recipe;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.anderes.cookbook.web.rest.dto.RecipeResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;

public class DtoHelper {

    /**
     * Bei dieser Aktualisierung wird im speziellen die Liste von Tags kombiniert. 
     * @param resource Resource welche die aktualisierten Daten enthält
     * @param recipe Dieses Objekt wird aktualisiert
     */
    public static void updateRecipe(final RecipeResource resource, final Recipe recipe) {
        Validate.notNull(resource);
        Validate.notNull(recipe);
        Validate.isTrue(resource.getUuid().equals(recipe.getUuid()));
        
        BeanUtils.copyProperties(resource, recipe, "uuid", "version", "lastUpdate", "addingDate");
        final List<String> tagsToDelete = recipe.getTags().stream().filter(t -> !resource.getTags().contains(t)).collect(Collectors.toList());
        tagsToDelete.forEach(t -> recipe.removeTag(t));
        resource.getTags().stream().filter(t -> !StringUtils.isBlank(t)).filter(t -> !recipe.getTags().contains(t)).forEach(t -> recipe.addTag(t));
        recipe.setLastUpdate(LocalDateTime.now());
    }

    public static void updateIngredient(IngredientResource resource, Ingredient ingredient) {
        Validate.notNull(resource);
        Validate.notNull(ingredient);
        Validate.isTrue(resource.getResourceId().equals(ingredient.getUuid()));
        
        ingredient.setAnnotation(resource.getComment());
        ingredient.setDescription(resource.getDescription());
        ingredient.setQuantity(resource.getPortion());
    }

}
