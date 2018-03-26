package org.anderes.edu.jpa.converter;

import org.anderes.edu.jpa.domain.Ingredient;
import org.anderes.edu.jpa.rest.dto.IngredientResource;
import org.springframework.core.convert.converter.Converter;

public class IngredientResourceToIngredientEntity implements Converter<Ingredient, IngredientResource> {

    @Override
    public IngredientResource convert(Ingredient source) {
        return new IngredientResource(source.getUuid(), source.getQuantity(), source.getDescription(), source.getAnnotation());
    }

}
