package org.anderes.web.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebSecurityConfig.class, WebMvcConfig.class })
@WebAppConfiguration
public class JwtAnonymousTokenGeneratorTest {

    @Inject
    private JwtAnonymousTokenGenerator anonymousTokenGenerator;
    @Value("${jwt.secret}")
    private String secret;
    
    @Test
    @SuppressWarnings("unchecked")
    public void shouldBeAnonymousToken() throws Exception {
        // given (warten nach der Instanzierung)
        Thread.currentThread();
        Thread.sleep(2000);
        
        // when
        final String token = anonymousTokenGenerator.createToken();
        
        // thean
        final Claims body = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        long difference = getExpiration().getTime() - body.getExpiration().getTime();
        System.out.println(difference + "ms");
        assertThat(difference < 1000, is(true));
        
        final Collection<String> collection = (Collection<String>) body.get("roles");
        assertThat(collection.contains("ROLE_ANONYMOUS"), is(true));
    }
    

    private Date getExpiration() {
        return convertToDate(LocalDateTime.now().plusHours(2L));
    }
    
    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
