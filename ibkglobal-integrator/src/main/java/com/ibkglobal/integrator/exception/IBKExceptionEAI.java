package com.ibkglobal.integrator.exception;

@SuppressWarnings("serial")
public class IBKExceptionEAI extends CommonException {
	
	public IBKExceptionEAI(ErrorType errorType) {
		super();
		setErrorType(errorType);
	}
	
	public IBKExceptionEAI(ErrorType errorType, String message) {
		super(message);
		setErrorType(errorType);
	}
	
	public IBKExceptionEAI(ErrorType errorType, Throwable cause) {
		super(cause);
		setErrorType(errorType);
	}
	
	public IBKExceptionEAI(ErrorType errorType, String message, Throwable cause) {
		super(message, cause);
		setErrorType(errorType);
	}
	
	public IBKExceptionEAI(ErrorType errorType, String message, Exception cause) {
		super(message, cause);
		setErrorType(errorType);
	}
}
