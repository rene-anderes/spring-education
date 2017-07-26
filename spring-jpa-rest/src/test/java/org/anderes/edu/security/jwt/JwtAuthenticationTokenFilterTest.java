package org.anderes.edu.security.jwt;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.anderes.edu.security.jwt.util.JwtTokenGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                "classpath:unittest-jwt-application-context.xml"
})
@WebAppConfiguration
public class JwtAuthenticationTokenFilterTest {

    @Inject
    private JwtAuthenticationTokenFilter filter;
    @Inject
    private JwtTokenGenerator jwtTokenGenerator;
    @Value("${jwt.header}")
    private String tokenHeader;
    private String token;
    
    @Before
    public void setup() {
        final JwtUserDto user = new JwtUserDto("Anderes", "Admin", "User");
        token = jwtTokenGenerator.generateToken(user);
    }
    
    @Test
    public void attemptAuthenticationTest() {
        
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(tokenHeader)).thenReturn("Bearer " + token);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // when
        Authentication authentication = filter.attemptAuthentication(request, response);
        
        // then
        assertThat(authentication ,is(not(nullValue())));
        assertThat(authentication.getName() ,is("Anderes"));
        
    }
    
    @Test
    public void attemptAnonymousTest() {
        
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(tokenHeader)).thenReturn("");
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // when
        Authentication authentication = filter.attemptAuthentication(request, response);
        
        // then
        assertThat(authentication ,is(not(nullValue())));
        assertThat(authentication.getName() ,is("anonymousUser"));
        
    }
}
