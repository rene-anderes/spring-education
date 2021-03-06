package org.anderes.cookbook.web.converter;

import org.anderes.cookbook.domain.Recipe;
import org.anderes.cookbook.web.rest.dto.RecipeResource;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

public class RecipeEntityToRecipeResource implements Converter<Recipe, RecipeResource> {

    @Override
    public RecipeResource convert(Recipe recipe) {
        final RecipeResource resource = new RecipeResource(recipe.getUuid());
        BeanUtils.copyProperties(recipe, resource, "uuid", "version", "editingDate", "addingDate");
        recipe.getTags().stream().forEach(t -> resource.addTag(t));
        resource.setAddingDate(recipe.getAddingDateTime()).setEditingDate(recipe.getLastUpdateTime());
        return resource;
    }

}
