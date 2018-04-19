package org.anderes.spring.configuration;

import java.util.HashSet;
import java.util.Set;

import org.anderes.cookbook.web.converter.IngredientEntityToIngredientResource;
import org.anderes.cookbook.web.converter.IngredientResourceToIngredientEntity;
import org.anderes.cookbook.web.converter.RecipeEntityToRecipeResource;
import org.anderes.cookbook.web.converter.RecipeResourceToRecipeEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class JUnitConverterConfig {

    @Bean
    public ConversionServiceFactoryBean getConversionServiceFactory() {
        final ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        final Set<Converter<?,?>> converters = new HashSet<>(4);
        converters.add(new RecipeResourceToRecipeEntity());
        converters.add(new RecipeEntityToRecipeResource());
        converters.add(new IngredientEntityToIngredientResource());
        converters.add(new IngredientResourceToIngredientEntity());
        conversionService.setConverters(converters);
        return conversionService;
    }
}
