package org.anderes.edu.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UsersServiceClient {
    
    @Autowired 
    @Qualifier("UsersRestUrl")
    private String url;
    private RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public List<String> getUser(final String username, final String password) throws Exception {

        final BasicAuthorizationInterceptor interceptor = new BasicAuthorizationInterceptor(username, password);
        try {
            restTemplate.getInterceptors().add(interceptor);
            final ResponseEntity<Token> response = restTemplate.postForEntity(url, null, Token.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                final String token = response.getBody().getToken();
                final Claims body = Jwts.parser().setSigningKey("my-very-secret-key".getBytes()).parseClaimsJws(token).getBody();
                final Collection<String> collection = (Collection<String>) body.get("roles");
                return new ArrayList<>(collection);
            }
            return new ArrayList<>(0);
        } finally {
            restTemplate.getInterceptors().remove(interceptor);
        }
    }

    public RestTemplate getTemplate() {
        return restTemplate;
    }

    public static class Token {
        private String token;

        String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }
    }
}
