package com.ivan.landroutingservice.exceptions;

public class UnknownCountryException extends RuntimeException {

    public UnknownCountryException(String message) {
        super(message);
    }
}
