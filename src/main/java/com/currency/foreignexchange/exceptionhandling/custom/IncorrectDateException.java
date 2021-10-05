package com.currency.foreignexchange.exceptionhandling.custom;

/**
 * The Custom Exception is used for incorrect dates.
 */
public class IncorrectDateException extends RuntimeException{
    public IncorrectDateException(String message) {
        super(message);
    }
}
