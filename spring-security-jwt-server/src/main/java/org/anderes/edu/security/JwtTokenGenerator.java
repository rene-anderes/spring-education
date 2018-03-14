package org.anderes.edu.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.anderes.edu.security.rest.TokenGenerator;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Singleton
public class JwtTokenGenerator implements TokenGenerator, InitializingBean {
    
    private final Logger logger = LoggerFactory.getLogger(JwtTokenGenerator.class);
    private final static long DEFAULT_EXPIRATION_HOURS = 24L;
    private LocalDateTime expirationDate = LocalDateTime.now().plusHours(DEFAULT_EXPIRATION_HOURS);
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
        Validate.notBlank(username);
        Validate.notNull(roles);
        final Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        return Jwts.builder().setClaims(claims)
                        .setExpiration(getExpiration())
                        .signWith(SignatureAlgorithm.forName(algorithm), secret.getBytes())
                        .compact();
    }

    @Override
    public TokenGenerator setExpiration(final LocalDateTime expirationDate) {
        Validate.notNull(expirationDate);
        this.expirationDate = expirationDate;
        return this;
    }

    private Date getExpiration() {
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
