package com.fdmgroup.fx_app.exceptions;

/**
 * Custom exception class for use in pre-validating transaction data.
 * This exception is thrown when a name received in connection with a transaction cannot be found in association with an existing User object.
 */
public class UserNotFoundException extends Exception {

    /**
     * Auto-generated UID for serialization.
     */
    private static final long serialVersionUID = 3185408914861024809L;

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message providing context for the exception
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}