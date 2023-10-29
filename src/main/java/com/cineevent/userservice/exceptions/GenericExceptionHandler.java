package com.cineevent.userservice.exceptions;

import org.springframework.http.HttpStatus;

import com.cineevent.userservice.dto.request.ErrorDTO;

public final class GenericExceptionHandler {
	
	private GenericExceptionHandler() {
		throw new IllegalStateException("Tranformer class");
	}
	
	public static ErrorDTO handleException(Exception e) {
		if(e instanceof InValidAccessTokenException) {
			return new ErrorDTO(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
		} else if(e instanceof AccessTokenExpiredException) {
			return new ErrorDTO(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
		} else if(e instanceof MissingAuthorizationHeaderException) {
			return new ErrorDTO(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
		} else {
			return new ErrorDTO("UnKnown Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
