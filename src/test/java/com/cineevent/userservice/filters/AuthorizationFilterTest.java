package com.cineevent.userservice.filters;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cineevent.userservice.dto.response.UserAuthResponseDTO;
import com.cineevent.userservice.exceptions.InValidAccessTokenException;
import com.cineevent.userservice.security.ThreadLocalAuthStore;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilterTest {

    @InjectMocks
    private AuthorizationFilter authorizationFilter;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternalWithAuthDetails() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        UserAuthResponseDTO authResponseDTO = new UserAuthResponseDTO();
        authResponseDTO.setUserId(1);
        authResponseDTO.setUserName("testUser");
        authResponseDTO.setUserRole("ROLE_USER");
        
        ThreadLocalAuthStore.setAuthDetails(authResponseDTO);


        authorizationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        assertEquals(1, authorities.size()); 
        for(GrantedAuthority authority : authorities) {
        	assertTrue(authority.getAuthority().equals("ROLE_USER"));
        }
      
    }

    @Test
    public void testDoFilterInternalWithNoAuthDetails() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        ThreadLocalAuthStore.setAuthDetails(null);
        Exception exception = assertThrows(InValidAccessTokenException.class, () -> authorizationFilter.doFilterInternal(request, response, filterChain));
        String expectedMessage = "Authorization header value is not correct";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

}

