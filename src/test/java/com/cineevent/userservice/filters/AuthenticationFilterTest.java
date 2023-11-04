package com.cineevent.userservice.filters;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import com.cineevent.userservice.dto.response.UserAuthResponseDTO;
import com.cineevent.userservice.exceptions.InValidAccessTokenException;
import com.cineevent.userservice.exceptions.MissingAuthorizationHeaderException;
import com.cineevent.userservice.security.AccessTokenGenerator;
import com.cineevent.userservice.security.ThreadLocalAuthStore;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilterTest {

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private AccessTokenGenerator accessTokenGenerator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternalWithValidToken() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/users");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validAccessToken");

        UserAuthResponseDTO authResponseDTO = new UserAuthResponseDTO();
        authResponseDTO.setUserId(1);
        authResponseDTO.setUserName("testUser");

        when(accessTokenGenerator.validateToken("validAccessToken")).thenReturn(authResponseDTO);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        assertTrue(ThreadLocalAuthStore.getAccessToken().equals("validAccessToken"));
        assertTrue(ThreadLocalAuthStore.getAuthDetails().getUserName().equals("testUser"));
        assertTrue(ThreadLocalAuthStore.getAuthDetails().getUserId() == 1);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalWithMissingAuthorizationHeader() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/users");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        Exception exception = assertThrows(MissingAuthorizationHeaderException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });
        
        String expectedMessage = "Authorization header is missing";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testDoFilterInternalWithInvalidAccessToken() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/users");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");

        Exception exception = assertThrows(InValidAccessTokenException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });
        String expectedMessage = "Authorization header value is not correct";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

}
