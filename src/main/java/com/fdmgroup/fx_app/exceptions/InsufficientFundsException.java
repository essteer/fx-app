package com.fdmgroup.fx_app.exceptions;

public class InsufficientFundsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7091737732659206725L;
	
	public InsufficientFundsException(String message) {
        super(message);
	}
}
