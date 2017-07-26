package org.anderes.edu.security.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

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
@ContextConfiguration(locations = {
                "classpath:application-context.xml",
                "classpath:unittest-jpa-context.xml",
                "classpath:unittest-application-context.xml",
                "classpath:unittest-security-context.xml"
})
@WebAppConfiguration
public class LoginControllerIT {

    @Inject
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
    }
    
    @Test
    public void shouldBeGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/token")
                        .with(httpBasic("admin", "password"))
                        .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8.toString()))
                    .andExpect(content().string(is(not(nullValue()))))
                    .andExpect(content().string(startsWith("{")))
                    .andReturn();
        final String content = result.getResponse().getContentAsString();
        System.out.println("Token: " + content);
    }
    
    @Test
    public void shouldBeGetTokenAnonymous() throws Exception {
        mockMvc.perform(post("/users/token")
                        .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isUnauthorized())
                    .andReturn();
    }
}
