package org.anderes.edu.security;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebSecurityTestConfig.class })
@WebAppConfiguration
public class UsersServiceClientTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private UsersServiceClient service;
    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXX0.57hogo_1kWUrMT0-h_qGlU_l89L0olz5uy7R5pKkSDDWKtOhiI29dBYCE0nT8vm6NwouonGUgihDxw8-ReHqvA";
    private MockRestServiceServer server;
    
    @Before
    public void setUp() throws Exception {
        server = MockRestServiceServer.bindTo(service.getTemplate()).build();
    }
 
    @Test
    public void shouldBeCorrectCall() throws Exception {
        // given
        final JsonObject jsonToken = Json.createObjectBuilder().add("token", token).build();
        server.expect(once(), requestTo("/users/token"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(jsonToken.toString(), APPLICATION_JSON));

        // when
        final List<String> roles = service.getRolesForUser("admin", "password");
        
        // then
        assertThat(roles, is(not(nullValue())));
        assertThat(roles.size(), is(2));
        assertThat(roles, hasItem("ROLE_ADMIN"));
        assertThat(roles, hasItem("ROLE_USER"));
    }
    
    @Test
    public void shouldBeHttpClientErrorException() throws Exception {
        // given
        thrown.expect(HttpClientErrorException.class);
        thrown.expectMessage("401 Unauthorized");
        
        server.expect(once(), requestTo("/users/token"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withUnauthorizedRequest());
        
        // when
        service.getRolesForUser("admin", "wrong");
    }
    
    @Test
    public void shouldBeHttpServerErrorException() throws Exception {
        // given
        thrown.expect(HttpServerErrorException.class);
        thrown.expectMessage(startsWith("503 JWT strings must contain"));
        
        final JsonObject jsonToken = Json.createObjectBuilder().add("token", "wrong_token").build();
        server.expect(once(), requestTo("/users/token"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(jsonToken.toString(), APPLICATION_JSON));

        // when
        service.getRolesForUser("admin", "password");
    }
}
