package org.anderes.web.configuration;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.anderes.web.security.JwtAuthenticationEntryPoint;
import org.anderes.web.security.JwtAuthenticationProvider;
import org.anderes.web.security.JwtAuthenticationSuccessHandler;
import org.anderes.web.security.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       
        http.authorizeRequests()    
                .antMatchers(POST, "/employees/*").hasRole("ADMIN")
                .antMatchers(PUT, "/employees/*").hasRole("ADMIN")
                .antMatchers(DELETE, "/employees/*").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .authenticationProvider(new JwtAuthenticationProvider())
                .httpBasic().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .addFilterAfter(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
        jwtAuthenticationTokenFilter.setAuthenticationManager(authenticationManager());
        jwtAuthenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        return jwtAuthenticationTokenFilter;
    }
}
