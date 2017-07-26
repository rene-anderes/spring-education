package org.anderes.edu.security.jwt.util;

import java.util.Collection;
import java.util.Optional;

import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenValidator {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tries to parse specified String as a JWT token.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or Empty if a token is invalid.
     */
    @SuppressWarnings("unchecked")
    public Optional<JwtUserDto> parseToken(String token) {
        JwtUserDto user = null;

        try {
            Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            user = new JwtUserDto();
            user.setUsername(body.getSubject()).setRoles((Collection<String>) body.get("roles"));

        } catch (JwtException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }
}
