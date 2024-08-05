package com.fx.fx_app.exceptions;

/**
 * This exception is thrown when source data files required
 * to execute the program are empty.
 */
public class DataSourceException extends Exception {

    /**
	 * Auto-generated UID for serialization.
	 */
	private static final long serialVersionUID = 5294837209837462318L;

    /**
	 * Constructs a new DataSourceException with the specified detail message.
	 *
	 * @param message the detail message providing context for the exception
	 */
	public DataSourceException(String message) {
        super(message);
	}

}
