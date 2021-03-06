package org.anderes.web.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.security.rest.TokenGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class JwtTokenGeneratorTest {

    @Inject
    private TokenGenerator generator;
    
    @Test
    public void shouldBeCorrectToken() {
       
        // when
        final String token = generator.createToken("user", "USER", "ADMIN");
        // then
        assertThat(token, is(not(nullValue())));
        assertThat(token.length(), is(183));
    }
    
    @Test
    public void shouldBeCorrectTokenWithExpiration() {

        // when
        final String token = generator.setExpiration(LocalDateTime.now().plusDays(1l)).createToken("user", "USER", "ADMIN");
        // then
        assertThat(token, is(not(nullValue())));
        assertThat(token.length(), is(183));
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void shouldBeNotCreatetToken() {
       
        generator.createToken(null, "USER", "ADMIN");
    }
}
