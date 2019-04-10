package com.mybank.app.exceptions;

public class InternalErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -449876336500097330L;

	public InternalErrorException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public InternalErrorException(String error) {
		super(error);
	}

	public InternalErrorException() {
		super();
	}
}
