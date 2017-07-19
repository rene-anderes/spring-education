package org.anderes.edu.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.anderes.edu.security.jwt.model.JwtAuthenticationToken;
import org.anderes.edu.security.jwt.util.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final static String BEARER = "Bearer ";
    @Value("${jwt.header}")
    private String tokenHeader;
    @Inject
    private JwtTokenGenerator generator;

    public JwtAuthenticationTokenFilter() {
        super("/**");
    }

    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
       
        Optional<String> header = Optional.ofNullable(request.getHeader(tokenHeader));
        final Optional<JwtAuthenticationToken> authentication = header.filter(h -> h.startsWith(BEARER))
                        .map(h -> {
                            String authToken = h.substring(BEARER.length());
                            return new JwtAuthenticationToken(authToken);
                        });
        Authentication authRequest = authentication.orElseGet(() -> {
            final String authToken = generator.generateTokenForAnonymous();
            return new JwtAuthenticationToken(authToken);
        });

        return getAuthenticationManager().authenticate(authRequest);
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}