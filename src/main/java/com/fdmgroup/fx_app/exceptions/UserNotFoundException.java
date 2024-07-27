package com.fdmgroup.fx_app.exceptions;

/**
 * Exception for use in pre-validating transaction data
 */
public class UserNotFoundException extends Exception {

	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = 3185408914861024809L;
	
	/**
	 * Arises when a name received in connection with a transaction cannot be found in association with an existing User object
	 * @param message provides context for each exception
	 */
	public UserNotFoundException(String message) {
        super(message);
    }

}
