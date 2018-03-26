package org.anderes.edu.jpa.rest;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        final ConstraintViolationException e = (ConstraintViolationException)ex;
        e.getConstraintViolations().stream()
            .map(c -> String.format("Bean Validation | Class '%s' | '%s': %s", c.getRootBeanClass(), c.getPropertyPath(), c.getMessage()))
            .forEach(System.err::println);
        String bodyOfResponse = "Entity for JPA not valid.";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), SERVICE_UNAVAILABLE, request);
    }
}
