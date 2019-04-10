package com.mybank.app.exceptions;

public class ConflictException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5770416056417274259L;

	public ConflictException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public ConflictException(String error) {
		super(error);
	}

	public ConflictException() {
		super();
	}
}
