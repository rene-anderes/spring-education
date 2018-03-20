package org.anderes.web.security.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringReader;

import javax.inject.Inject;
import javax.json.Json;
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
public class EmployeesControllerTest {

    @Inject
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    private final String tokenHeader = "Authorization";
    private String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQU5PTllNT1VTIl19.y6tlAJI6yVMfzG6_GTW3-58S8NILBsofa5kqzQBxPWJ8YPGX_RkGpuyPhVJ0hsrmRBtiZzRm9Wy92Uir2a6iJQ";
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
    }
    
    @Test
    public void shouldBeGetOneEmployee() throws Exception {
   
        final MvcResult result = mockMvc.perform(get("/employees/Gates")
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
        assertThat(parser.getString(), is("firstname"));
        event = parser.next();
        assertThat(event, is(Event.VALUE_STRING));
        assertThat(parser.getString(), is("Bill"));
        event = parser.next();
        assertThat(event, is(Event.KEY_NAME));
        assertThat(parser.getString(), is("lastname"));
        event = parser.next();
        assertThat(event, is(Event.VALUE_STRING));
        assertThat(parser.getString(), is("Gates"));
        event = parser.next();
        assertThat(event, is(Event.END_OBJECT));
        parser.close();
    }
    
    @Test
    public void shouldBeGetOneEmployeeWithToken() throws Exception {
   
        mockMvc.perform(get("/employees/Gates")
                        .header(tokenHeader, token)
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8.toString()))
                    .andExpect(content().string(is(not(nullValue()))))
                    .andExpect(content().string(startsWith("{")))
                    .andReturn();
    }
    
    @Test
    public void shouldBeDeleteOneEmployee() throws Exception {
        
        mockMvc.perform(delete("/employees/Gates").header(tokenHeader, token))
                    .andExpect(status().isOk())
                    .andReturn();
    }
    
    @Test
    public void shouldBeDeleteUnauthorizied() throws Exception {
        
        mockMvc.perform(delete("/employees/Gates").header(tokenHeader, "Bearer hfdlksahkjgfdhsghskjd"))
                    .andExpect(status().isUnauthorized())
                    .andReturn();
    }
    
    @Test
    public void shouldBeDeleteWithoutTokenIsForbidden() throws Exception {
        
        mockMvc.perform(delete("/employees/Gates"))
                    .andExpect(status().isForbidden())
                    .andReturn();
    }
}
