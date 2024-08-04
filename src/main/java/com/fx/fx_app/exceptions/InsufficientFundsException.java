package com.fx.fx_app.exceptions;

/**
 * Custom exception class for use in pre-validating transaction data.
 * This exception is thrown when a User holds insufficient funds of a given currency to perform a transaction.
 */
public class InsufficientFundsException extends Exception {

	/**
	 * Auto-generated UID for serialization.
	 */
	private static final long serialVersionUID = 7091737732659206725L;
	
	/**
	 * Constructs a new InsufficientFundsException with the specified detail message.
	 *
	 * @param message the detail message providing context for the exception
	 */
	public InsufficientFundsException(String message) {
        super(message);
	}
}