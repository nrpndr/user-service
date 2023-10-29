package com.cineevent.userservice.exceptions;

public class UserDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = 1636844910414930397L;

	public UserDoesNotExistException(String message) {
        super(message);
    }
}
