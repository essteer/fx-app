package com.fx.fx_app.exceptions;

/**
 * Custom exception class for use in pre-validating transaction data.
 * This exception is thrown when a currency code received in connection with a transaction cannot be found in association with an existing Currency object.
 */
public class InvalidCurrencyException extends Exception {

    /**
     * Auto-generated UID for serialization.
     */
    private static final long serialVersionUID = -943765332319315083L;

    /**
     * Constructs a new InvalidCurrencyException with the specified detail message.
     *
     * @param message the detail message providing context for the exception
     */
    public InvalidCurrencyException(String message) {
        super(message);
    }
}