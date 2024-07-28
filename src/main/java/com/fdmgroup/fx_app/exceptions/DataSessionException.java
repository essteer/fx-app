package com.fdmgroup.fx_app.exceptions;

/**
 * Custom exception class for errors related to the {@link DataSession} class.
 * This exception is thrown when there is an attempt to access a DataSession instance prior to its initialization.
 */
public class DataSessionException extends RuntimeException {
	
	/**
	 * Auto-generated UID for serialization.
	 */
	private static final long serialVersionUID = 2025372730739616414L;

	/**
	 * Constructs a new DataSessionException with the specified detail message.
	 *
	 * @param message the detail message providing context for the exception
	 */
	public DataSessionException(String message) {
        super(message);
    }
}
