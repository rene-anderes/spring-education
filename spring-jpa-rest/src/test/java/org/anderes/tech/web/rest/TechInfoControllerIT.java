package org.anderes.tech.web.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.anderes.spring.configuration.DatabaseConfig;
import org.anderes.spring.configuration.JpaConfig;
import org.anderes.spring.configuration.WebMvcConfig;
import org.junit.Before;
import org.junit.Ignore;
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
@ContextConfiguration(classes = { WebMvcConfig.class, DatabaseConfig.class })
@WebAppConfiguration
public class TechInfoControllerIT {

    @Inject
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
   
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }
    
    @Test
    public void shouldBeDatabaseInfo() throws Exception {
        MvcResult result = mockMvc.perform(get("/tech/info/database")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("Product-Name", is("Apache Derby")))
                        .andExpect(jsonPath("Product-Version", is("10.14.1.0 - (1808820)")))
                        .andExpect(jsonPath("Driver-Name", is("Apache Derby Embedded JDBC Driver")))
                        .andExpect(jsonPath("Driver-Version", is("10.14.1.0 - (1808820)")))
                        .andExpect(jsonPath("URL", is("jdbc:derby:memory:myDB")))
                        .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    @Test
    public void shouldBeSpringInfo() throws Exception{
        System.out.println(mockMvc.perform(get("/tech/info/spring")
                        .accept(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("Spring-Version", is("5.0.5.RELEASE")))
                        .andReturn().getResponse().getContentAsString());
    }
}
