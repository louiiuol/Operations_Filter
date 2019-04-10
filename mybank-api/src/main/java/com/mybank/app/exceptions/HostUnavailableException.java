package com.mybank.app.exceptions;

public class HostUnavailableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7454694167122090855L;

	public HostUnavailableException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public HostUnavailableException(String error) {
		super(error);
	}

	public HostUnavailableException() {
		super();
	}
}
