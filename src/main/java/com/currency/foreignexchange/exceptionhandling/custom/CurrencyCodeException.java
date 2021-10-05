package com.currency.foreignexchange.exceptionhandling.custom;

/**
 * Custom Exception class used for wrong currency code errors.
 */
public class CurrencyCodeException extends IllegalArgumentException {
    public CurrencyCodeException(String message) {
        super(message);
    }
}
