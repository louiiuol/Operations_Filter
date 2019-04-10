package com.mybank.app.exceptions;

public class BadInputException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9095062097093822402L;

	public BadInputException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public BadInputException(String error) {
		super(error);
	}
}
