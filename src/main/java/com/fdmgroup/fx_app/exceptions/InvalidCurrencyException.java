package com.fdmgroup.fx_app.exceptions;

/**
 * Exception for use in pre-validating transaction data
 */
public class InvalidCurrencyException extends Exception {

	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = -943765332319315083L;
	
	/**
	 * Arises when a currency code received in connection with a transaction cannot be found in association with an existing Currency object
	 * @param message provides context for each exception
	 */
	public InvalidCurrencyException(String message) {
        super(message);
	}

}
