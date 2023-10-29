package com.cineevent.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cineevent.userservice.dto.response.ErrorResponseDTO;
import com.cineevent.userservice.exceptions.AccessTokenExpiredException;
import com.cineevent.userservice.exceptions.InValidAccessTokenException;
import com.cineevent.userservice.exceptions.InValidTokenInputException;
import com.cineevent.userservice.exceptions.InValidUserInputException;
import com.cineevent.userservice.exceptions.InValidUserLoginInputException;
import com.cineevent.userservice.exceptions.MissingAuthorizationHeaderException;
import com.cineevent.userservice.exceptions.UserDoesNotExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ InValidUserInputException.class, InValidUserLoginInputException.class, InValidTokenInputException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponseDTO> handleAsBadRequest(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ InValidAccessTokenException.class, AccessTokenExpiredException.class,
			MissingAuthorizationHeaderException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponseDTO> handleInValidTokenException(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler({ UserDoesNotExistException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponseDTO> handleAsNotFound(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
