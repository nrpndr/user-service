package com.cineevent.userservice.exceptions;

public class InValidUserInputException extends RuntimeException{

	private static final long serialVersionUID = 1636844910414930397L;

	public InValidUserInputException(String message) {
        super(message);
    }
}