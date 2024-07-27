package com.fdmgroup.fx_app.exceptions;

/**
 * Exception for @see DataSession class
 */
public class DataSessionException extends RuntimeException {
	
	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = 2025372730739616414L;

	/**
	 * Arises following an attempt to access a DataSession instance prior to initialisation
	 * @param message provides context for each exception
	 */
	public DataSessionException(String message) {
        super(message);
    }

}
