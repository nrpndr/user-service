package com.cineevent.userservice.exceptions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.cineevent.userservice.dto.request.ErrorDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GenericExceptionHandlerTest {

    @Test
    public void testHandleInValidAccessTokenException() {
        Exception exception = new InValidAccessTokenException("Invalid access token");

        ErrorDTO errorDTO = GenericExceptionHandler.handleException(exception);

        assertNotNull(errorDTO);
        assertEquals("Invalid access token", errorDTO.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorDTO.getStatusCode());
    }

    @Test
    public void testHandleAccessTokenExpiredException() {
        Exception exception = new AccessTokenExpiredException("Access token expired");

        ErrorDTO errorDTO = GenericExceptionHandler.handleException(exception);

        assertNotNull(errorDTO);
        assertEquals("Access token expired", errorDTO.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorDTO.getStatusCode());
    }

    @Test
    public void testHandleMissingAuthorizationHeaderException() {
        Exception exception = new MissingAuthorizationHeaderException("Missing authorization header");

        ErrorDTO errorDTO = GenericExceptionHandler.handleException(exception);

        assertNotNull(errorDTO);
        assertEquals("Missing authorization header", errorDTO.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorDTO.getStatusCode());
    }

    @Test
    public void testHandleUnknownException() {
        Exception exception = new RuntimeException("Unknown exception");

        ErrorDTO errorDTO = GenericExceptionHandler.handleException(exception);

        assertNotNull(errorDTO);
        assertEquals("UnKnown Error", errorDTO.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDTO.getStatusCode());
    }
}

