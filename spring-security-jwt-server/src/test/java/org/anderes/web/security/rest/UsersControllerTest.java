package org.anderes.web.security.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringReader;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebSecurityConfig.class, WebMvcConfig.class })
@WebAppConfiguration
public class UsersControllerTest {

    @Inject
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
    }
    
    @Test
    public void shouldBePutUpdateUser() throws Exception {
        
        final JsonArray roles = Json.createArrayBuilder().add("ROLE_USER").add("ROLE_ADMIN").build();
        final JsonObject user = Json.createObjectBuilder().add("username", "userEdit").add("password", "newPassword").add("roles", roles).build();
        
        mockMvc.perform(put("/users/userEdit")
                        .with(httpBasic("admin", "password"))
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(user.toString()))
            .andExpect(status().isOk())
            .andReturn();
        
        mockMvc.perform(get("/users/user")
                        .with(httpBasic("userEdit", "newPassword"))
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8.toString()))
                    .andExpect(content().string(is(not(nullValue()))))
                    .andExpect(content().string(startsWith("{")))
                    .andReturn();
    }
    
    @Test
    public void shouldBePutBadRequest() throws Exception {
        
        final JsonArray roles = Json.createArrayBuilder().add("ROLE_USER").add("ROLE_ADMIN").build();
        final JsonObject user = Json.createObjectBuilder().add("username", "userBad").add("password", "newPassword").add("roles", roles).build();
 
        mockMvc.perform(put("/users/userEdit")
                        .with(httpBasic("admin", "password"))
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(user.toString()))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
    
    @Test
    public void shouldBeDeleteUser() throws Exception {
        
        mockMvc.perform(delete("/users/user@delete")
                        .with(httpBasic("admin", "password")))
                    .andExpect(status().isOk())
                    .andReturn();
                
        mockMvc.perform(delete("/users/user@delete")
                        .with(httpBasic("admin", "password")))
                    .andExpect(status().isNotFound())
                    .andReturn();
        
        mockMvc.perform(get("/users/user@delete")
                        .with(httpBasic("admin", "password"))
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound())
                    .andReturn();
       
    }
    
    @Test
    public void shouldBePostNewUser() throws Exception {
        
        final JsonArray roles = Json.createArrayBuilder().add("ROLE_USER").build();
        final JsonObject user = Json.createObjectBuilder().add("username", "Stephen").add("password", "Hawking").add("roles", roles).build();
        
        mockMvc.perform(post("/users")
                        .with(httpBasic("admin", "password"))
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(user.toString()))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("http://localhost/users/Stephen")))
            .andReturn();
    }
    
    @Test
    public void shouldBePostBadRequestNotValid() throws Exception {
        
        final JsonArray roles = Json.createArrayBuilder().add("ROLE_USER").build();
        final JsonObject user = Json.createObjectBuilder().add("username", "Peter").add("password", "").add("roles", roles).build();
        
        mockMvc.perform(post("/users")
                        .with(httpBasic("admin", "password"))
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(user.toString()))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
    
    @Test
    public void shouldBePostBadRequestDuplicateUser() throws Exception {
        
        final JsonArray roles = Json.createArrayBuilder().add("ROLE_USER").build();
        final JsonObject user = Json.createObjectBuilder().add("username", "user").add("password", "password").add("roles", roles).build();
        
        mockMvc.perform(post("/users")
                        .with(httpBasic("admin", "password"))
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(user.toString()))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
    
    @Test
    public void shouldBeGetUser() throws Exception {
        
        final MvcResult result = mockMvc.perform(get("/users/user")
                        .with(httpBasic("admin", "password"))
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8.toString()))
                    .andExpect(content().string(is(not(nullValue()))))
                    .andExpect(content().string(startsWith("{")))
                    .andReturn();
        
        // Dump Result
        final String content = result.getResponse().getContentAsString();
        System.out.println("User: " + content);
        
        final JsonParser parser = Json.createParser(new StringReader(content));
        Event event = parser.next();
        assertThat(event, is(Event.START_OBJECT));
        event = parser.next();
        assertThat(event, is(Event.KEY_NAME));
        assertThat(parser.getString(), is("username"));
        event = parser.next();
        assertThat(event, is(Event.VALUE_STRING));
        assertThat(parser.getString(), is("user"));
        event = parser.next();
        assertThat(event, is(Event.KEY_NAME));
        assertThat(parser.getString(), is("roles"));
        event = parser.next();
        assertThat(event, is(Event.START_ARRAY));
        event = parser.next();
        assertThat(parser.getString(), is("ROLE_ANONYMOUS"));
        event = parser.next();
        assertThat(parser.getString(), is("ROLE_USER"));
        event = parser.next();
        assertThat(event, is(Event.END_ARRAY));
        event = parser.next();
        assertThat(event, is(Event.END_OBJECT));
        parser.close();
    }
    
    @Test
    public void shouldBeGetForbidden() throws Exception {
        
        mockMvc.perform(get("/users/user")
                        .with(httpBasic("user", "password"))
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden())
                    .andReturn();
    }
    
    @Test
    public void shouldBeGetUnauthorized() throws Exception {
        
        mockMvc.perform(get("/users/user")
                        .with(httpBasic("bill", "password"))
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isUnauthorized())
                    .andReturn();
    }
}
