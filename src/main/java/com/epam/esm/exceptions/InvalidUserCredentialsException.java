package com.epam.esm.exceptions;

/**
 * UserNotFoundExceptionClass
 *
 * @author Vlad Storoshchuk
 */
public class InvalidUserCredentialsException extends RuntimeException{
    public InvalidUserCredentialsException(String message) {
        super(message);
    }
}