package com.currency.foreignexchange.exceptionhandling.custom;

/**
 * The Custom Exception is used for incorrect dates.
 */
public class IncorrectDateException extends IllegalArgumentException{
    public IncorrectDateException(String message) {
        super(message);
    }
}
