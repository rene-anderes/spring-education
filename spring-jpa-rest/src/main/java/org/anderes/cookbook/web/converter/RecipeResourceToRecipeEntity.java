package org.anderes.cookbook.web.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.anderes.cookbook.domain.Recipe;
import org.anderes.cookbook.web.rest.dto.RecipeResource;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

public class RecipeResourceToRecipeEntity implements Converter<RecipeResource, Recipe> {

    @Override
    public Recipe convert(RecipeResource resource) {
        Recipe recipe;
        if (resource.getUuid() == null) {
            recipe = new Recipe();
        } else {
            recipe = new Recipe(resource.getUuid());
        }
        BeanUtils.copyProperties(resource, recipe, "uuid", "version", "lastUpdate", "addingDate");
        recipe.setAddingDate(longToLocatDateTime(resource.getAddingDate()));
        recipe.setLastUpdate(longToLocatDateTime(resource.getEditingDate()));
        resource.getTags().stream().forEach(t -> recipe.addTag(t));
        return recipe;
    }

    private LocalDateTime longToLocatDateTime(final Long datetime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(datetime), ZoneId.systemDefault());
    }
}
