package com.ibkglobal.integrator.exception;

@SuppressWarnings("serial")
public class IBKExceptionAPI extends CommonException {
	
	public IBKExceptionAPI(ErrorType errorType) {
		super();
		setErrorType(errorType);
	}
	
	public IBKExceptionAPI(ErrorType errorType, String message) {
		super(message);
		setErrorType(errorType);
	}
	
	public IBKExceptionAPI(ErrorType errorType, Throwable cause) {
		super(cause);
		setErrorType(errorType);
	}
	
	public IBKExceptionAPI(ErrorType errorType, String message, Throwable cause) {
		super(message, cause);
		setErrorType(errorType);
	}
	
	public IBKExceptionAPI(ErrorType errorType, String message, Exception cause) {
		super(message, cause);
		setErrorType(errorType);
	}
}
