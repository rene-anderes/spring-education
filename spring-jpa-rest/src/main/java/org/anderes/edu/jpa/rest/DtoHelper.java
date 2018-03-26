package org.anderes.edu.jpa.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class DtoHelper {

    public static void updateRecipe(final RecipeResource resource, final Recipe recipe) {
        BeanUtils.copyProperties(resource, recipe, "uuid", "version", "lastUpdate", "addingDate");
        final List<String> tagsToDelete = recipe.getTags().stream().filter(t -> !resource.getTags().contains(t)).collect(Collectors.toList());
        tagsToDelete.forEach(t -> recipe.removeTag(t));
        resource.getTags().stream().filter(t -> !StringUtils.isBlank(t)).filter(t -> !recipe.getTags().contains(t)).forEach(t -> recipe.addTag(t));
        recipe.setLastUpdate(LocalDateTime.now());
    }

}
