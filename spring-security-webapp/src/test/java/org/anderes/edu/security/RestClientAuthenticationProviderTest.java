package org.anderes.edu.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebSecurityConfig.class })
@WebAppConfiguration
@ActiveProfiles({"alternativ", "mock"})
public class RestClientAuthenticationProviderTest {
    
    @Autowired
    private UsersServiceClient userService;
    @Autowired
    private RestClientAuthenticationProvider authenticationProvider;

    @Before
    public void setup() throws Exception {
        when(userService.getRolesForUser("admin", "password")).thenReturn(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
    }
    
    @Test
    public void shouldBeCorrectList() {
        // given
        final Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password");
        // when
        final Authentication result = authenticationProvider.authenticate(authentication);
        // then
        assertThat(result.getAuthorities(), is(not(nullValue())));
        final Collection<? extends GrantedAuthority> roles = result.getAuthorities();
        final Iterator<? extends GrantedAuthority> iterator = roles.iterator();
        assertThat(roles.size(), is(2));
        final GrantedAuthority role1 = iterator.next();
        assertThat(role1.getAuthority(), is("ROLE_ADMIN"));
        final GrantedAuthority role2 = iterator.next();
        assertThat(role2.getAuthority(), is("ROLE_USER"));
    }
    
    @Test
    public void shouldBeEmptyList() {
        // given
        final Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "wrong");
        // when
        final Authentication result = authenticationProvider.authenticate(authentication);
        // then
        assertThat(result.getAuthorities(), is(not(nullValue())));
        assertThat(result.getAuthorities().isEmpty(), is(true));
    }
}
