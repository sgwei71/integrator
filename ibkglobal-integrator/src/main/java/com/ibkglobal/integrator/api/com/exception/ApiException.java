package com.ibkglobal.integrator.api.com.exception;

@SuppressWarnings("serial")
public class ApiException extends Exception {
	
	public ApiException() {
		super();
	}

	public ApiException(String message) {
		super(message);
	}
}
