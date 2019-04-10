package com.mybank.app.exceptions;

public class DBConnectionFailedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 140659327387194701L;

	public DBConnectionFailedException(String error, Throwable throwed) {
		super(error, throwed);
	}

	public DBConnectionFailedException(String error) {
		super(error);
	}

}