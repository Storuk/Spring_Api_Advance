package com.epam.esm.exceptions.controlleradvice;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
    @ExceptionHandler(value = {ItemNotFoundException.class, UserNotFoundException.class})
    protected ResponseEntity<?> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.NOT_FOUND, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Method for handling NullPointerException, SQLSyntaxErrorException, InvalidDataException, ConstraintViolationException
     */
    @ExceptionHandler(value = {ConstraintViolationException.class, InvalidDataException.class})
    protected ResponseEntity<?> handleInvalidDataException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.BAD_REQUEST, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<?> handleUserInvalidDataException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.FORBIDDEN, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> handleNullPointerException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of("message", ex.getLocalizedMessage())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}