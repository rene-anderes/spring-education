package org.anderes.cookbook.web.converter;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.springframework.core.convert.converter.Converter;

public class IngredientEntityToIngredientResource implements Converter<IngredientResource, Ingredient> {

    @Override
    public Ingredient convert(IngredientResource source) {
        if (source.getResourceId() == null) {
            return new Ingredient(source.getPortion(), source.getDescription(), source.getComment());
        }
        return new Ingredient(source.getResourceId(), source.getPortion(), source.getDescription(), source.getComment());
    }

}
