package com.cineevent.userservice.exceptions;

public class InValidUserLoginInputException extends RuntimeException{

	private static final long serialVersionUID = 1636844910414930397L;

	public InValidUserLoginInputException(String message) {
        super(message);
    }
}