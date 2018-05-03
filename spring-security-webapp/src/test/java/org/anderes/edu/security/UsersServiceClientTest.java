package org.anderes.edu.security;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebSecurityConfig.class })
@WebAppConfiguration
@ActiveProfiles("testing")
public class UsersServiceClientTest {
    
    @Autowired
    private UsersServiceClient service;
    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXX0.57hogo_1kWUrMT0-h_qGlU_l89L0olz5uy7R5pKkSDDWKtOhiI29dBYCE0nT8vm6NwouonGUgihDxw8-ReHqvA";
 
    @Before
    public void setUp() throws Exception {
        final MockRestServiceServer server = MockRestServiceServer.bindTo(service.getTemplate()).build();
        final JsonObject jsonToken = Json.createObjectBuilder().add("token", token).build();
         
        server.expect(requestTo("/users/token"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(jsonToken.toString(), MediaType.APPLICATION_JSON));
    }
 
    @Test
    public void shhluldBeCorrectCalll() throws Exception {

        // when
        final List<String> roles = service.getUser("admin", "password");
        
        // then
        assertThat(roles, is(not(nullValue())));
        assertThat(roles.size(), is(2));
        assertThat(roles, hasItem("ROLE_ADMIN"));
        assertThat(roles, hasItem("ROLE_USER"));
    }
}
