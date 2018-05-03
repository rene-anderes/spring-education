package org.anderes.web.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@Singleton
public class JwtAnonymousTokenGenerator implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JwtAnonymousTokenGenerator.class);
    private final static long DEFAULT_EXPIRATION_HOURS = 2L;
    @Value("${jwt.algorithm}")
    private String algorithm;
    @Value("${jwt.secret}")
    private String secret;

    public String createToken() {
        final Claims claims = Jwts.claims().setSubject("ANONYMOUS");
        claims.put("roles", Stream.of("ROLE_ANONYMOUS").collect(Collectors.toSet()));
        return Jwts.builder().setClaims(claims)
                        .setExpiration(getExpiration())
                        .signWith(SignatureAlgorithm.forName(algorithm), secret.getBytes())
                        .compact();
    }

    private Date getExpiration() {
        return convertToDate(LocalDateTime.now().plusHours(DEFAULT_EXPIRATION_HOURS));
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
