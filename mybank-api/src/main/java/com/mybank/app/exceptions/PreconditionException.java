package com.mybank.app.exceptions;

public class PreconditionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8927016930833944782L;

	public PreconditionException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public PreconditionException(String error) {
		super(error);
	}

	public PreconditionException() {
		super();
	}
}
