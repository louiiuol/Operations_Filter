package com.mybank.app.exceptions;

public class UnauthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5635456097081830617L;

	public UnauthorizedException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public UnauthorizedException(String error) {
		super(error);
	}

	public UnauthorizedException() {
		super();
	}
}
