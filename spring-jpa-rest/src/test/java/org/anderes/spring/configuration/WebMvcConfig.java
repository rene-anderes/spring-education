package org.anderes.spring.configuration;

import java.util.ArrayList;
import java.util.List;

import org.anderes.cookbook.domain.RecipeRepository;
import org.anderes.cookbook.domain.RecipeRepositoryStub;
import org.anderes.cookbook.web.converter.IngredientEntityToIngredientResource;
import org.anderes.cookbook.web.converter.IngredientResourceToIngredientEntity;
import org.anderes.cookbook.web.converter.RecipeEntityToRecipeResource;
import org.anderes.cookbook.web.converter.RecipeResourceToRecipeEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages = { "org.anderes.cookbook.web.rest" } )
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

    @Bean
    public RecipeRepository getRecipeRepository() {
        return new RecipeRepositoryStub();
    }
    
    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
        
    @Bean
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        final RequestMappingHandlerAdapter handler = new RequestMappingHandlerAdapter();
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>(1);
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        handler.setMessageConverters(messageConverters );
        return handler;
    }
}
