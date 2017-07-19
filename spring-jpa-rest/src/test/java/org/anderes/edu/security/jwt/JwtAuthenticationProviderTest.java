package org.anderes.edu.security.jwt;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import javax.inject.Inject;

import org.anderes.edu.security.jwt.exception.JwtTokenMalformedException;
import org.anderes.edu.security.jwt.model.AuthenticatedUser;
import org.anderes.edu.security.jwt.model.JwtAuthenticationToken;
import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.anderes.edu.security.jwt.util.JwtTokenGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                "classpath:unittest-jwt-application-context.xml"
})
public class JwtAuthenticationProviderTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Inject
    private JwtAuthenticationProvider authenticationProvider;
    @Inject
    private JwtTokenGenerator jwtTokenGenerator;
    private String token;
    
    @Before
    public void setup() {
        final JwtUserDto user = new JwtUserDto(42L, "Anderes", "Admin", "User");
        token = jwtTokenGenerator.generateToken(user);
    }
    
    @Test
    public void retrieveUserTest() {
        
        // given
        final UsernamePasswordAuthenticationToken authentication = new JwtAuthenticationToken(token);
        
        // when
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authenticationProvider.retrieveUser("Anderes", authentication);
        
        // then
        assertThat(authenticatedUser, is(not(nullValue())));
        assertThat(authenticatedUser.getId(), is(42L));
        assertThat(authenticatedUser.getToken(), is(token));
        assertThat(authenticatedUser.getUsername(), is("Anderes"));
        assertThat(authenticatedUser.getAuthorities().size(), is(2));
        final Iterator<? extends GrantedAuthority> i = authenticatedUser.getAuthorities().iterator();
        assertThat(i.next().getAuthority(), is("User"));
        assertThat(i.next().getAuthority(), is("Admin"));
    }
    
    @Test
    public void invalidTokenTest() {
        
        // given
        final UsernamePasswordAuthenticationToken authentication = new JwtAuthenticationToken(token + "invalid");
        thrown.expect(JwtTokenMalformedException.class);
        thrown.expectMessage("JWT token is not valid");
        
        // when
        authenticationProvider.retrieveUser("Anderes", authentication);
    }
}
