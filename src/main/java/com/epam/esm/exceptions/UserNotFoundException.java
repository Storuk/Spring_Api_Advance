package com.epam.esm.exceptions;

/**
 * UserNotFoundExceptionClass
 *
 * @author Vlad Storoshchuk
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
