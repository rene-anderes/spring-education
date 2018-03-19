package org.anderes.web.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebSecurityConfig.class, WebMvcConfig.class })
@WebAppConfiguration
public class JwtAuthenticationTokenFilterTest {

    @Inject
    private JwtAuthenticationTokenFilter filter;
    
    @Value("${jwt.header}")
    private String tokenHeader;
    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BTk9OWU1PVVMiXSwiZXhwIjoxNTIxNTQ5NDY2fQ.0SRQKIqoB2VHzjD8SkfGbi3gTtDKkwpMcCK42RXKODX-Gz1UaB7AqZPJEfIoxwbga_TY6KbPkMR0fBAmXBra2A";
    
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
        assertThat(authentication.getName() ,is("user"));
        
    }
    
    @Test @Ignore
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
