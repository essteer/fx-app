package com.fdmgroup.fx_app.exceptions;

/**
 * Exception for use in pre-validating transaction data
 */
public class InsufficientFundsException extends Exception {

	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = 7091737732659206725L;
	
	/**
	 * Arises when a User holds insufficient funds of a given Currency to perform a transaction
	 * @param message provides context for each exception
	 */
	public InsufficientFundsException(String message) {
        super(message);
	}
}
