package com.ibkglobal.integrator.exception;

@SuppressWarnings("serial")
public class IBKExceptionFEP extends CommonException {
	
	public IBKExceptionFEP(ErrorType errorType) {
		super();
		setErrorType(errorType);
	}
	
	public IBKExceptionFEP(ErrorType errorType, String message) {
		super(message);
		setErrorType(errorType);
	}
	
	public IBKExceptionFEP(ErrorType errorType, Throwable cause) {
		super(cause);
		setErrorType(errorType);
	}
	
	public IBKExceptionFEP(ErrorType errorType, String message, Throwable cause) {
		super(message, cause);
		setErrorType(errorType);
	}
	
	public IBKExceptionFEP(ErrorType errorType, String message, Exception cause) {
		super(message, cause);
		setErrorType(errorType);
	}
}
