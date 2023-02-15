package com.epam.esm.exceptions;

/**
 * ItemNotFoundExceptionClass
 * @author Vlad Storoshchuk
 * */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
