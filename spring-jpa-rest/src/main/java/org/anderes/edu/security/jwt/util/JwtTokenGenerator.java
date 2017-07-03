package org.anderes.edu.security.jwt.util;

import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenGenerator {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.anonymous.user}")
    private String anonymousUser;
    @Value("${jwt.anonymous.role}")
    private String anonymousRole;
    
    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims.
     * These properties are taken from the specified User object. Tokens validity is infinite.
     *
     * @param user the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(JwtUserDto user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId() + "");
        claims.put("roles", user.getRoles());

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Generates a JWT token for anonymous
     * @return the JWT token
     */
    public String generateTokenForAnonymous() {
        final JwtUserDto user = new JwtUserDto(Long.MAX_VALUE, anonymousUser, anonymousRole);
        return generateToken(user);
    }
}
