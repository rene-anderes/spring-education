package org.anderes.edu.jpa.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

import org.anderes.edu.dbunitburner.DbUnitRule;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSet;
import org.anderes.edu.jpa.rest.dto.IngredientResource;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                "classpath:application-context.xml",
                "classpath:unittest-jpa-context.xml",
                "classpath:unittest-application-context.xml",
                "classpath:unittest-security-context.xml"
})
@WebAppConfiguration
@UsingDataSet(value = { "/data/prepare.json" })
public class RecipeControllerIT {

    @Inject
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    
    @Inject @Rule 
    public DbUnitRule dbUnitRule;
    @Value("${jwt.header}")
    private String tokenHeader;
    private String token = "Bearer ";
  
    @Before
    public void setUp() {
        assertThat("Bitte Spring-Konfiguration überprüfen.", tokenHeader, is(not("${jwt.header}")));
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
        try {
            MvcResult result = mockMvc.perform(post("/users/login")
                            .content(getUserPassword().toJSONString())
                            .contentType(APPLICATION_JSON_UTF8)
                            .accept(APPLICATION_JSON_UTF8))
                        .andExpect(status().isOk())
                        .andReturn();
            JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            token += (String) parser.parse(result.getResponse().getContentAsString(), JSONObject.class).get("token");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private JSONObject getUserPassword() {
        final JSONObject o = new JSONObject();
        o.putIfAbsent("name", "user");
        o.putIfAbsent("password", "password");
        return o;
    }
    
    @Test
    public void shouldBeAllRecipes() throws Exception {
        MvcResult result = mockMvc.perform(get("/recipes")
                        .accept(APPLICATION_JSON)
                        .param("size", "10"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.content", hasSize(3)))
                        .andExpect(jsonPath("$.totalElements", is(3)))
                        .andExpect(jsonPath("$.content[0].uuid", is("c0e5582e-252f-4e94-8a49-e12b4b047afb")))
                        .andExpect(jsonPath("$.content[0].links[0].rel", is("self")))
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
                        .andExpect(jsonPath("$.uuid", is("c0e5582e-252f-4e94-8a49-e12b4b047afb")))
                        .andExpect(jsonPath("$.title", is("Arabische Spaghetti")))
                        .andExpect(jsonPath("$.addingDate", is(1390428200000L)))
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
                        .header(tokenHeader, token)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(recipeToSave)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", containsString("http://localhost/recipes/")))
                        .andReturn();
    }
    
    @Test
    public void shouldBeSaveNewRecipePUT() throws Exception {
        final RecipeResource recipeToSave = createRecipeWithUUID();
        mockMvc.perform(put("/recipes/" + recipeToSave.getUuid() + "?updateDate=false")
                        .header(tokenHeader, token)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(recipeToSave)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", containsString("http://localhost/recipes/" + recipeToSave.getUuid())))
                        .andReturn();
    }
    
    @Test
    public void shouldBeUpdateRecipe() throws Exception {
        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb")
                        .header(tokenHeader, token)
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk()).andReturn();
        final ObjectMapper mapper = new ObjectMapper();
        final RecipeResource resource = mapper.readValue(result.getResponse().getContentAsString(), RecipeResource.class);
        resource.setRating(3);

        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb")
                        .header(tokenHeader, token)
                        .content(convertObjectToJsonBytes(resource))
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk()).andReturn();
    }
    
    @Test
    public void shouldBeDeleteRecipe() throws Exception {
        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7")
                        .header(tokenHeader, token))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();
        mockMvc.perform(get("/recipes").accept(APPLICATION_JSON).param("limit", "50"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalElements", is(2)))
                        .andReturn();
    }

    @Test
    public void shouldBeIngredientsFormOneRecipe() throws Exception {

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
    public void shouldBeFindOneIngredient() throws Exception {

        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047112")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.*", hasSize(5)))
                        .andExpect(jsonPath("$.resourceId", is("c0e5582e-252f-4e94-8a49-e12b4b047112")))
                        .andExpect(jsonPath("$.portion", is("250g")))
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeNotFoundOneIngredient() throws Exception {

        final MvcResult result = mockMvc.perform(get("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/101A")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
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
        mockMvc.perform(post("/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7/ingredients").header(tokenHeader, token)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(newIngredient)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", containsString("http://localhost/recipes/adf99b55-4804-4398-af4e-e37ec2c692c7/ingredients/")))
                        .andReturn();
    }
    
    @Test
    public void shouldBeUpdateIngredient() throws Exception {
        
        final IngredientResource ingredient = new IngredientResource("c0e5582e-252f-4e94-8a49-e12b4b047112", "250g", "Spaghetti", "Bio");
        
        mockMvc.perform(put("/recipes/c0e5582e-252f-4e94-8a49-e12b4b047afb/ingredients/" + ingredient.getResourceId())
                        .header(tokenHeader, token)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(ingredient)))
                        .andExpect(status().isOk())
                        .andReturn();
    }
    
    @Test
    public void shouldBeDeleteIngredient() throws Exception {

        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af55-e37ec2c692ff/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047211")
                        .header(tokenHeader, token))
                        .andExpect(status().isOk())
                        .andReturn();

        mockMvc.perform(delete("/recipes/adf99b55-4804-4398-af55-e37ec2c692ff/ingredients/c0e5582e-252f-4e94-8a49-e12b4b047211")
                        .header(tokenHeader, token))
                        .andExpect(status().isOk())
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

    private Long december(int day, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.DECEMBER, day);
        return cal.getTime().getTime();
    }
    
}
