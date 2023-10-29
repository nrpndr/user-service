package com.cineevent.userservice.exceptions;

public class InValidTokenInputException extends RuntimeException {

	private static final long serialVersionUID = 1636844910414930397L;

	public InValidTokenInputException(String message) {
        super(message);
    }
}
