package com.mybank.app.exceptions;

public class IdMissingException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4315120616959958122L;

	public IdMissingException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public IdMissingException(String error) {
		super(error);
	}
	
	public IdMissingException() {
		super();
	}

}
