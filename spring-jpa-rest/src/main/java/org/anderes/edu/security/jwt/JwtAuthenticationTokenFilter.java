package org.anderes.edu.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.anderes.edu.security.jwt.exception.JwtTokenMissingException;
import org.anderes.edu.security.jwt.model.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final static String BEARER = "Bearer ";
    @Value("${jwt.header}")
    private String tokenHeader;

    public JwtAuthenticationTokenFilter() {
        super("/**");
    }

    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        String header = request.getHeader(tokenHeader);

        if (header == null || !header.startsWith(BEARER)) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }

        String authToken = header.substring(BEARER.length());

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);

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