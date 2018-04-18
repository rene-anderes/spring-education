package org.anderes.cookbook.web.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import javax.inject.Inject;

import org.anderes.cookbook.domain.RecipeRepositoryStub;
import org.anderes.cookbook.web.rest.dto.IngredientResource;
import org.anderes.cookbook.web.rest.dto.RecipeResource;
import org.anderes.spring.configuration.DatabaseConfig;
import org.anderes.spring.configuration.WebMvcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class, DatabaseConfig.class })
@WebAppConfiguration
public class RecipeControllerIT {

    @Inject
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    @Inject
    private RecipeRepositoryStub repository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        repository.initialize();
    }

    @Test
    public void shouldBeAllRecipes() throws Exception {
        MvcResult result = mockMvc.perform(get("/recipes")
                        .accept(APPLICATION_JSON)
                        .param("size", "2"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("content", hasSize(4)))
                        .andExpect(jsonPath("totalElements", is(4)))
                        .andExpect(jsonPath("content[0].uuid", is("c0e5582e-252f-4e94-8a49-e12b4b047afb")))
                        .andExpect(jsonPath("content[0].links[0].rel", is("self")))
                        .andExpect(jsonPath("totalPages", is(2)))
                        .andExpect(jsonPath("totalElements", is(4)))
                        .andExpect(jsonPath("last", is(false)))
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeOneRecipe() throws Exception {

        MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("uuid", is("c0e5582e-252f-4e94-8a49-e12b4b047afb")))
                        .andExpect(jsonPath("title", is("Arabische Spaghetti")))
                        .andExpect(jsonPath("addingDate", is(1390428200000L)))
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeNotFindRecipe() throws Exception {
        
        mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047xxx").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();
    }
    
    @Test
    public void shouldBeSaveNewRecipePOST() throws Exception {
        final RecipeResource recipeToSave = createRecipeWithoutUUID();
        mockMvc.perform(post("/recipes")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(recipeToSave)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", containsString("http://localhost/recipes/")))
                        .andReturn();
    }
    
    @Test
    public void shouldBeNOTSaveNewRecipePOST() throws Exception {
        final RecipeResource recipeToSave = createInvalidRecipe();
        mockMvc.perform(post("/recipes")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(recipeToSave)))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }
    
    @Test
    public void shouldBeSaveNewRecipePUT() throws Exception {
        final RecipeResource recipeToSave = createRecipeWithUUID();
        mockMvc.perform(put("/recipes/" + recipeToSave.getUuid() + "?updateDate=false")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(recipeToSave)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", is("http://localhost/recipes/" + recipeToSave.getUuid())))
                        .andReturn();
    }
    
    @Test
    public void shouldBeUpdateRecipePUT() throws Exception {
        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk()).andReturn();
        final ObjectMapper mapper = new ObjectMapper();
        final RecipeResource resource = mapper.readValue(result.getResponse().getContentAsString(), RecipeResource.class);
        resource.setRating(3);

        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb")
                        .content(convertObjectToJsonBytes(resource))
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk()).andReturn();
    }

    /**
     * Die ID der Resource entspricht nicht der ID in der URL
     */
    @Test
    public void shouldBeNotUpdateRecipePUT() throws Exception {

        final RecipeResource resource = new RecipeResource("c0e5582e-252f-4e94-8a49-notSame");
        resource.setTitle("junit").setNoOfPerson("2").setPreamble("......");
        
        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb")
                        .content(convertObjectToJsonBytes(resource ))
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest()).andReturn();
    }
    
    @Test
    public void shouldBeDeleteRecipe() throws Exception {
        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7"))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();
        mockMvc.perform(get("/recipes").accept(APPLICATION_JSON).param("limit", "50"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("totalElements", is(3)))
                        .andReturn();
    }

    @Test
    public void shouldBeAllIngredientsByRecipeId() throws Exception {

        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.*", hasSize(3)))
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeNotFindIngredientsByRecipeId() throws Exception {

        mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047sss/ingredients")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }
    
    @Test
    public void shouldBeFindOneIngredient() throws Exception {

        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047112")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.*", hasSize(5)))
                        .andExpect(jsonPath("resourceId", is("c0e5582e-252f-4e94-8a49-e12b4b047112")))
                        .andExpect(jsonPath("description", is("Spaghetti")))
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeNotFindOneIngredientWrongRecipeId() throws Exception {

        mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047sss/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047112")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }
    
    @Test
    public void shouldBeNotFoundOneIngredient() throws Exception {

        mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/101A")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }
    
    @Test
    public void shouldBeNotFindOneIngredient() throws Exception {
        
        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/1012233")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeSaveNewIngredient() throws Exception {
        
        final IngredientResource newIngredient = new IngredientResource("1g", "Salz", "beliebig");
        mockMvc.perform(post("/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7/ingredients")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(newIngredient)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", containsString("http://localhost/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7/ingredients/")))
                        .andReturn();
    }
    
    @Test
    public void shouldBeNOTSaveNewIngredientWrongData() throws Exception {
        
        final IngredientResource newIngredient = new IngredientResource("1g", null, "beliebig");
        mockMvc.perform(post("/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7/ingredients")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(newIngredient)))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }
    
    @Test
    public void shouldBeNOTSaveNewIngredientWrongRecipeId() throws Exception {
        
        final IngredientResource newIngredient = new IngredientResource("1g", "Salz", "beliebig");
        mockMvc.perform(post("/recipes/adf99b55-4804-4398-af4e-e37ec2c69sss/ingredients")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(newIngredient)))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }
    
    @Test
    public void shouldBeUpdateIngredient() throws Exception {
        
        final IngredientResource ingredient = new IngredientResource("c0e5582e-252f-4e94-8a49-e12b4b047112", "250g", "Spaghetti", "Bio");
        
        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/" + ingredient.getResourceId())
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(ingredient)))
                        .andExpect(status().isOk())
                        .andReturn();
    }
    
    @Test
    public void shouldBeUpdateIngredientWithNullValue() throws Exception {
        
        final IngredientResource ingredient = new IngredientResource("c0e5582e-252f-4e94-8a49-e12b4b047112", null, "Spaghetti", null);
        
        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/" + ingredient.getResourceId())
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(ingredient)))
                        .andExpect(status().isOk())
                        .andReturn();
    }
    
    @Test
    public void shouldBeNotUpdateIngredientWrongRecipeId() throws Exception {
        
        final IngredientResource ingredient = new IngredientResource("c0e5582e-252f-4e94-8a49-e12b4b047112", "250g", "Spaghetti", "Bio");
        
        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047sss/ingredients/" + ingredient.getResourceId())
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(ingredient)))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }
    
    @Test
    public void shouldBeNotUpdateIngredientWrongIngredient() throws Exception {
        
        final IngredientResource ingredient = new IngredientResource("c0e5582e-252f-4e94-8a49-e12b4b047112", null, "Spaghetti", null);
        
        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/abcWrong")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(ingredient)))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }
    
    @Test
    public void shouldBePUTNewIntegrient() throws Exception {
        
        final IngredientResource ingredient = new IngredientResource("c0e5582e-252f-4e94-8a49-e12b4b0asdfg", null, "Spaghetti", null);
        
        mockMvc.perform(put("/recipes/aaa99b55-4804-4398-af55-e37ec2c692ff/ingredients/c0e5582e-252f-4e94-8a49-e12b4b0asdfg")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(ingredient)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", is("http://localhost/recipes/aaa99b55-4804-4398-af55-e37ec2c692ff/ingredients/c0e5582e-252f-4e94-8a49-e12b4b0asdfg")))
                        .andReturn();
    }
    
    @Test
    public void shouldBeDeleteIngredient() throws Exception {

        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af55-e37ec2c692ff/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047211"))
                        .andExpect(status().isOk())
                        .andReturn();

        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af55-e37ec2c692ff/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047211"))
                        .andExpect(status().isOk())
                        .andReturn();
    }
    
    @Test
    public void shouldBeNotDeleteIngredientWrongRecipeId() throws Exception {

        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af55-e37ec2c69sss/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047211"))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }
    
    @Test
    public void shouldBeGetAllTags() throws Exception {

        final MvcResult result = mockMvc.perform(get("/recipes/tags")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.*", hasSize(4)))
                        .andReturn();

        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    private byte[] convertObjectToJsonBytes(RecipeResource object) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return mapper.writeValueAsBytes(object);
    }
    
    private String convertObjectToJsonBytes(IngredientResource object) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return mapper.writeValueAsString(object);
    }
    
    private RecipeResource createRecipeWithUUID() {
        final RecipeResource recipe = new RecipeResource(UUID.randomUUID().toString());
        recipe.setTitle("Neues Rezept vom Junit-Test").setPreamble("Da gibt es einiges zu sagen")
            .setAddingDate(december(24, 2014)).setEditingDate(december(29, 2014)).setNoOfPerson("2")
            .setPreparation("Die Zubereitung ist einfach").setRating(4).addTag("pasta").addTag("new");
        return recipe;
    }
    
    private RecipeResource createRecipeWithoutUUID() {
        final RecipeResource recipe = new RecipeResource();
        recipe.setTitle("Omeletten-Gemüse-Gratin").setPreamble("Omeletten hausgemacht")
            .setAddingDate(december(24, 2014)).setEditingDate(december(29, 2014)).setNoOfPerson("2")
            .setPreparation("Mehl, Eier, Salz (ca. 1/4 TL) und die Herbes de Provence in eine Schüssel geben und glatt rühren.").setRating(4).addTag("vegi").addTag("new");
        return recipe;
    }

    private RecipeResource createInvalidRecipe() {
        final RecipeResource recipe = new RecipeResource();
        recipe/*.setTitle("Omeletten-Gemüse-Gratin")*/.setPreamble("Omeletten hausgemacht")
            .setAddingDate(december(24, 2014)).setEditingDate(december(29, 2014)).setNoOfPerson("2")
            .setPreparation("Mehl, Eier, Salz (ca. 1/4 TL) und die Herbes de Provence in eine Schüssel geben und glatt rühren.").setRating(4).addTag("vegi").addTag("new");
        return recipe;
    }
    
    private Long december(int day, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.DECEMBER, day);
        return cal.getTime().getTime();
    }
    
}
