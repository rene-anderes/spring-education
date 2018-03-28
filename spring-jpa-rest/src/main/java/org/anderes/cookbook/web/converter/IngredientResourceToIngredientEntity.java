package org.anderes.cookbook.web.converter;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.springframework.core.convert.converter.Converter;

public class IngredientResourceToIngredientEntity implements Converter<Ingredient, IngredientResource> {

    @Override
    public IngredientResource convert(Ingredient source) {
        return new IngredientResource(source.getUuid(), source.getQuantity(), source.getDescription(), source.getAnnotation());
    }

}
