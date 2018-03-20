package org.anderes.web.security;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAnonymousTokenGenerator.class);
    private final static String BEARER = "Bearer ";
    private final static String TOKEN_HEADER = "Authorization";
    @Inject
    private JwtAnonymousTokenGenerator generator;

    public JwtAuthenticationTokenFilter() {
        super("/**");
    }

    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
       
        final Optional<String> header = Optional.ofNullable(request.getHeader(TOKEN_HEADER));
        final Optional<JwtAuthenticationToken> authentication = 
                        header.filter(h -> h.startsWith(BEARER)).map(h -> new JwtAuthenticationToken(h.substring(BEARER.length())));
        if (authentication.isPresent()) {
            final String msg = String.format("request-path '%s' with valid JWT called.", request.getPathInfo());
            logger.debug(msg);
            return getAuthenticationManager().authenticate(authentication.get());
        }
        // JWT for anonymous
        final String msg = String.format("request-path '%s' without JWT (anonymous) called.", request.getPathInfo());
        logger.debug(msg);
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(generator.createToken()));
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