package com.epam.esm.exceptions;

/**
 * InvalidDataExceptionClass
 *
 * @author Vlad Storoshchuk
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
