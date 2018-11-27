package com.ibkglobal.integrator.api.com.exception.controllerAdvice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.ibkglobal.integrator.api.com.exception.ApiException;
import com.ibkglobal.integrator.api.com.model.ResultResponse;
import com.ibkglobal.integrator.api.com.model.ResultResponse.Error;
import com.ibkglobal.integrator.api.com.model.ResultResponse.Error.ErrorFactory;
import com.ibkglobal.integrator.log.LogManager;
import com.ibkglobal.integrator.log.LogType;

@ControllerAdvice
@RestController
public class ApiExceptionhandler {

	@Autowired
	private ErrorFactory errorFactory;
	
	@ExceptionHandler(value = ApiException.class)
	public ResultResponse apiException(Exception e) {
		Error error = errorFactory.getError(e);
		
		LogManager.getLogger(LogType.EXCEPTION).error(error.toString());
		return new ResultResponse(false, error);
	}
}
