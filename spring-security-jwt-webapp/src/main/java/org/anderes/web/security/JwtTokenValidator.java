package org.anderes.web.security;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenValidator {

    final Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);
    
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tries to parse specified String as a JWT token.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or Empty if a token is invalid.
     */
    @SuppressWarnings("unchecked")
    public Optional<UserDetails> parseToken(String token) {
        try {
            final Claims body = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
            final Collection<String> collection = (Collection<String>) body.get("roles");
            return Optional.of(new JwtUser(body.getSubject(), token, collection));
        } catch (JwtException e) {
            logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
