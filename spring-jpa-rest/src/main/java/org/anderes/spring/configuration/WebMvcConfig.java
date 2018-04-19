package org.anderes.spring.configuration;

import java.util.List;

import org.anderes.cookbook.web.converter.IngredientEntityToIngredientResource;
import org.anderes.cookbook.web.converter.IngredientResourceToIngredientEntity;
import org.anderes.cookbook.web.converter.RecipeEntityToRecipeResource;
import org.anderes.cookbook.web.converter.RecipeResourceToRecipeEntity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages = { "org.anderes.cookbook.web.rest", "org.anderes.tech" } )
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RecipeResourceToRecipeEntity());
        registry.addConverter(new RecipeEntityToRecipeResource());
        registry.addConverter(new IngredientEntityToIngredientResource());
        registry.addConverter(new IngredientResourceToIngredientEntity());
    }

    /**
     *  Handles HTTP GET requests for '/resources/**' by efficiently 
     *  serving up static resources in the '${webappRoot}/resources/' directory
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // Pageable für die REST-Schnittstelle
        resolvers.add(new PageableHandlerMethodArgumentResolver());
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        registry.viewResolver(viewResolver);
    }
}
