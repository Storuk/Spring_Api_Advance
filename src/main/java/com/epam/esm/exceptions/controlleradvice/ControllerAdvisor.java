package com.epam.esm.exceptions.controlleradvice;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.exceptions.InvalidUserCredentialsException;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

/**
 * Class ControllerAdvisor for handling exceptions
 *
 * @author Vlad Storoshchuk
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Method for handling ItemNotFoundException
     */
    @ExceptionHandler(value = {ItemNotFoundException.class})
    protected ResponseEntity<?> handleNotFoundException(ItemNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.NOT_FOUND, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Method for handling InvalidDataException, ConstraintViolationException
     */
    @ExceptionHandler(value = {ConstraintViolationException.class, InvalidDataException.class})
    protected ResponseEntity<?> handleInvalidDataException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.BAD_REQUEST, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Method for handling AccessDeniedException
     */
    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.FORBIDDEN, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    /**
     * Method for handling AuthenticationException
     */
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.UNAUTHORIZED, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
    /**
     * Method for handling InvalidUserCredentialsException
     */
    @ExceptionHandler({InvalidUserCredentialsException.class})
    public ResponseEntity<?> handleInvalidUserCredentialsException(InvalidUserCredentialsException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    /**
     * Method for handling FeignException
     */
    @ExceptionHandler({FeignException.class})
    public ResponseEntity<?> handleFeignException(FeignException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of("message", "invalid token")),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    /**
     * Method for handling others Exception
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> handleOtherException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}