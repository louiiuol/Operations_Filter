package com.mybank.app.exceptions;

public class NotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7981492704748030843L;

	public NotFoundException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public NotFoundException(String error) {
		super(error);
	}

	public NotFoundException() {
		super();
	}
}
