package org.anderes.edu.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

public class JwtTokenGeneratorTest {


    @Test
    public void shouldBeCorrectToken() {
        // given
        final JwtTokenGenerator generator = new JwtTokenGenerator();
        // when
        final String token = generator.createToken("user", "USER", "ADMIN");
        // then
        assertThat(token, is(not(nullValue())));
        assertThat(token.length(), is(183));
    }
    
    @Test
    public void shouldBeCorrectTokenWithExpiration() {
        // given
        final JwtTokenGenerator generator = new JwtTokenGenerator();
        // when
        final String token = generator.setExpiration(LocalDateTime.now().plusDays(1l)).createToken("user", "USER", "ADMIN");
        // then
        assertThat(token, is(not(nullValue())));
        assertThat(token.length(), is(183));
    }
}
