package org.anderes.edu.security;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class UsersServiceClient {
    
    @Autowired 
    @Qualifier("UsersRestUrl")
    private String url;
    private RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public List<String> getRolesForUser(final String username, final String password) throws RestClientException {

        final BasicAuthorizationInterceptor interceptor = new BasicAuthorizationInterceptor(username, password);
        final LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        try {
            restTemplate.getInterceptors().add(interceptor);
            restTemplate.getInterceptors().add(loggingInterceptor);
            final ResponseEntity<Token> response = restTemplate.postForEntity(url, null, Token.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                final String token = response.getBody().getToken();
                final Claims body = Jwts.parser().setSigningKey("my-very-secret-key".getBytes()).parseClaimsJws(token).getBody();
                final Collection<String> collection = (Collection<String>) body.get("roles");
                return new ArrayList<>(collection);
            }
            return new ArrayList<>(0);
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new HttpServerErrorException(SERVICE_UNAVAILABLE, e.getMessage());
        } finally {
            restTemplate.getInterceptors().remove(interceptor);
            restTemplate.getInterceptors().remove(loggingInterceptor);
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
    
    public static class LoggingInterceptor implements ClientHttpRequestInterceptor {
        private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            logger.trace("===========================request begin================================================");
            logger.trace("URI         : {}", request.getURI());
            logger.trace("Method      : {}", request.getMethod());
            logger.trace("Headers     : {}", request.getHeaders() );
            logger.trace("Request body: {}", new String(body, "UTF-8"));
            logger.trace("==========================request end================================================");
            return execution.execute(request, body);
        }
        
    }
}
