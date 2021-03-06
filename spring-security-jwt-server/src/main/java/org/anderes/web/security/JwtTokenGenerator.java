package org.anderes.web.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

import org.anderes.web.security.rest.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Singleton
@Validated
public class JwtTokenGenerator implements TokenGenerator, InitializingBean {
    
    private final Logger logger = LoggerFactory.getLogger(JwtTokenGenerator.class);
    private final static long DEFAULT_EXPIRATION_HOURS = 24L;
    private LocalDateTime expirationDate = LocalDateTime.MIN;
    @Value("${jwt.algorithm}")
    private String algorithm;
    @Value("${jwt.secret}")
    private String secret;

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
                        .signWith(SignatureAlgorithm.forName(algorithm), secret.getBytes())
                        .compact();
    }

    @Override
    public TokenGenerator setExpiration(@NotNull LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    private Date getExpiration() {
        if (expirationDate == LocalDateTime.MIN) {
            return convertToDate(LocalDateTime.now().plusHours(DEFAULT_EXPIRATION_HOURS));
        }
        return convertToDate(expirationDate);
    }
    
    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (algorithm == null || secret == null) {
            throw new IllegalStateException("@Value wurde nicht gesetzt.");
        }
        logger.debug("SignatureAlgorithm: " + algorithm);
    }

}
