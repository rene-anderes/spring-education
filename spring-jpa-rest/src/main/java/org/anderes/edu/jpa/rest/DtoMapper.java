package org.anderes.edu.jpa.rest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.springframework.beans.BeanUtils;

public class DtoMapper {

    public static void map(final RecipeResource resource, final Recipe recipe) {
        BeanUtils.copyProperties(resource, recipe, "uuid", "version", "editingDate", "addingDate");
        recipe.setLastUpdate(LocalDateTime.ofInstant(Instant.ofEpochMilli(resource.getEditingDate()), ZoneId.systemDefault()));
        recipe.setAddingDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(resource.getAddingDate()), ZoneId.systemDefault()));
        final List<String> tagsToDelete = recipe.getTags().stream().filter(t -> !resource.getTags().contains(t)).collect(Collectors.toList());
        tagsToDelete.forEach(t -> recipe.removeTag(t));
        resource.getTags().stream().filter(t -> !recipe.getTags().contains(t)).forEach(t -> recipe.addTag(t));
    }

}
