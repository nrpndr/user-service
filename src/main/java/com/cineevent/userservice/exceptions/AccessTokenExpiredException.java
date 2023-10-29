package com.cineevent.userservice.exceptions;

public class AccessTokenExpiredException extends RuntimeException {

	private static final long serialVersionUID = 1636844910414930397L;

	public AccessTokenExpiredException(String message) {
        super(message);
    }
	
}
