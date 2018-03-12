package org.anderes.edu.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.anderes.edu.security.jwt.rest.TokenGenerator;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenGenerator implements TokenGenerator {
    
    private final static long DEFAULT_EXPIRATION_DAYS = 1L;
    private LocalDateTime expirationDate = LocalDateTime.now().plusDays(DEFAULT_EXPIRATION_DAYS);

    @Override
    public String createToken(String username, String... roles) {
        return createToken(username, Arrays.stream(roles).collect(Collectors.toSet()));
    }

    @Override
    public String createToken(String username, Set<String> roles) {
        final Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        return Jwts.builder().setClaims(claims)
                        .setExpiration(getExpiration())
                        .signWith(SignatureAlgorithm.HS512, "gdsgfdshgfdhgdhg65474")
                        .compact();
    }

    @Override
    public TokenGenerator setExpiration(final LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    private Date getExpiration() {
        return convertToDate(expirationDate);
    }
    
    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
