package org.anderes.cookbook.web.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.inject.Inject;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.anderes.spring.configuration.JUnitConverterConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JUnitConverterConfig.class })
public class IngredientResourceToIngredientEntityTest {

    @Inject
    private ConversionService conversionService;

    @Test
    public void shouldBeConvert() {
        // given
        final String resourceId = UUID.randomUUID().toString();
        final IngredientResource resource = new IngredientResource(resourceId, "1 EL", "Mehl", "Bio");
        
        // when
        final Ingredient ingredient = conversionService.convert(resource, Ingredient.class);
        
        // then
        assertThat(ingredient, is(not(nullValue())));
        assertThat(ingredient.getUuid(), is(resourceId));
        assertThat(ingredient.getQuantity(), is("1 EL"));
        assertThat(ingredient.getDescription(), is("Mehl"));
        assertThat(ingredient.getAnnotation(), is("Bio"));
    }
    
    @Test
    public void shouldBeConvertWithoutResourceId() {
        // given
        final IngredientResource resource = new IngredientResource("1 EL", "Mehl", "Bio");
        
        // when
        final Ingredient ingredient = conversionService.convert(resource, Ingredient.class);
        
        // then
        assertThat(ingredient, is(not(nullValue())));
        assertThat(ingredient.getUuid(), is(not(nullValue())));
        assertThat(ingredient.getQuantity(), is("1 EL"));
        assertThat(ingredient.getDescription(), is("Mehl"));
        assertThat(ingredient.getAnnotation(), is("Bio"));
    }
}
