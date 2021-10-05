package com.currency.foreignexchange.exceptionhandling.custom;

import javassist.NotFoundException;

public class ElementNotFoundException extends NotFoundException {
    public ElementNotFoundException(String msg) {
        super(msg);
    }
}
