package org.anderes.cookbook.web.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.inject.Inject;

import org.anderes.cookbook.domain.Ingredient;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:converter-context.xml" })
public class IngredientEntityToIngredientResourceTest {

    @Inject
    private ConversionService conversionService;

    @Test
    public void shouldBeConvert() {
        // given
        final String uuid = UUID.randomUUID().toString();
        final Ingredient ingredient = new Ingredient(uuid, "100g", "Mehl", "Bio-Qualität");
        
        // when
        final IngredientResource resource = conversionService.convert(ingredient, IngredientResource.class);
        
        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getResourceId(), is(uuid));
        assertThat(resource.getPortion(), is("100g"));
        assertThat(resource.getDescription(), is("Mehl"));
        assertThat(resource.getComment(), is("Bio-Qualität"));
    }
}
