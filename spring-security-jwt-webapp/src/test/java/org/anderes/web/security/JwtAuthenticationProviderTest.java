package org.anderes.web.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebSecurityConfig.class, WebMvcConfig.class })
@WebAppConfiguration
public class JwtAuthenticationProviderTest {

    @Inject
    private JwtAuthenticationProvider provider;
    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BTk9OWU1PVVMiXX0.HIOTpQfLQCNIStlQ13vApGfeWUJNvVoMCe4ENC9BWZhkt3mBgJ_qKbmW67aKUifLSnAyjouiSGlRNgivBn4KVA";
    private String token_role_admin = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQU5PTllNT1VTIl19.y6tlAJI6yVMfzG6_GTW3-58S8NILBsofa5kqzQBxPWJ8YPGX_RkGpuyPhVJ0hsrmRBtiZzRm9Wy92Uir2a6iJQ";
    
    @Test 
    public void shouldBeCorrectUser() {
        // given
        final UsernamePasswordAuthenticationToken authentication = new JwtAuthenticationToken(token);
        
        // when
        final UserDetails user = provider.retrieveUser("user", authentication);
        
        // then
        assertThat(user, is(not(nullValue())));
        assertThat(user.getUsername(), is("user"));
        assertThat(user.getAuthorities().size(), is(2));
        assertThat(user.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_USER")).findFirst().isPresent(), is(true));
        assertThat(user.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ANONYMOUS")).findFirst().isPresent(), is(true));
        assertThat(user.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent(), is(false));
    }
    
    @Test 
    public void shouldBeCorrectUserRoleAdmin() {
        // given
        final UsernamePasswordAuthenticationToken authentication = new JwtAuthenticationToken(token_role_admin);
        
        // when
        final UserDetails user = provider.retrieveUser("user", authentication);
        
        // then
        assertThat(user, is(not(nullValue())));
        assertThat(user.getUsername(), is("user"));
        assertThat(user.getAuthorities().size(), is(2));
        assertThat(user.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_USER")).findFirst().isPresent(), is(false));
        assertThat(user.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ANONYMOUS")).findFirst().isPresent(), is(true));
        assertThat(user.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent(), is(true));
    }
}
