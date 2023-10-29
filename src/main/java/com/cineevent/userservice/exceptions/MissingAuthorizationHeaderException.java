package com.cineevent.userservice.exceptions;

public class MissingAuthorizationHeaderException extends RuntimeException {

	private static final long serialVersionUID = 1636844910414930397L;

	public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}
