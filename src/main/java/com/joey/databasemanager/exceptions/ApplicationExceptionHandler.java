package com.joey.databasemanager.exceptions;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler({ApplicationException.class,AsyncRequestTimeoutException.class})
	public String handler() {
		System.out.println("in global exception handler");
		return "error";
	}
	
	@ExceptionHandler({OfficeNotFoundException.class})
	public ResponseEntity<ApiError> officeNotFoundHandler() {
		
		return new ResponseEntity<ApiError>(new ApiError("OfficeNotFound"), new HttpHeaders(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({EmployeeNotFoundException.class})
	public ResponseEntity<ApiError> employeeNotFoundHandler() {
		
		return new ResponseEntity<ApiError>(new ApiError("EmployeeNotFound"), new HttpHeaders(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ConversionFailedException.class})
	public ResponseEntity<ApiError> conversionFailed() {
		return new ResponseEntity<>(new ApiError("Conversion failed. Invalid unit given"), 
				new HttpHeaders(),
				HttpStatus.BAD_REQUEST);
	}
	
}
