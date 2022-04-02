package com.joey.databasemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class OfficeNotFoundException extends RuntimeException {
	
	public OfficeNotFoundException(String message) {
		super(message);
	}
}
