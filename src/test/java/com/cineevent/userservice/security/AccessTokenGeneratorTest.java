package com.cineevent.userservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.cineevent.userservice.cache.UserCache;
import com.cineevent.userservice.configuration.RabbitMQConfig;
import com.cineevent.userservice.controller.MessageController;
import com.cineevent.userservice.dto.response.UserAuthResponseDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.exceptions.AccessTokenExpiredException;
import com.cineevent.userservice.exceptions.InValidAccessTokenException;
import com.cineevent.userservice.exceptions.UserDoesNotExistException;
import com.cineevent.userservice.messaging.MQMessageConsumer;

@SpringBootTest
@ComponentScan(basePackages = { "com.cineevent.userservice" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { RabbitMQConfig.class,
				MessageController.class, MQMessageConsumer.class }) })
@ExtendWith(MockitoExtension.class)
public class AccessTokenGeneratorTest {

	private String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJucnBuZHIiLCJpYXQiOjE2OTg1NzQxMDUsInVzZXJSb2xlIjoiQURNSU4iLCJ1c2VySWQiOjEsImV4cCI6MTY5ODU3NzcwNX0.vqGP-xuUA_GfkGa8yX2LI7GGJpu9U-vSIZapN2c9LW4";
	
	private String invalidToken = "invalidToken";
	

    @InjectMocks
    private AccessTokenGenerator accessTokenGenerator;

    @Mock
    private UserCache userCache;

    @Test
    public void testGenerateToken() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1);
        user.setUserName("testuser");
        user.setUserRole("ROLE_USER");
        String token = accessTokenGenerator.generateToken(user);
        assertNotNull(token);
    }

    @Test
    public void testValidateTokenWithValidToken() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1);
        user.setUserName("testuser");
        user.setUserRole("ROLE_USER");

        when(userCache.getUser(1)).thenReturn(user);
        String validToken = accessTokenGenerator.generateToken(user);
        UserAuthResponseDTO authResponseDTO = accessTokenGenerator.validateToken(validToken);

        assertEquals(user.getId(), authResponseDTO.getUserId());
        assertEquals(user.getUserName(), authResponseDTO.getUserName());
        assertEquals(user.getUserRole(), authResponseDTO.getUserRole());

        verify(userCache, times(1)).getUser(1);
    }

    @Test
    public void testValidateTokenWithExpiredToken() {
        assertThrows(AccessTokenExpiredException.class, () -> accessTokenGenerator.validateToken(expiredToken));
    }

    @Test
    public void testValidateTokenWithInvalidToken() {
        assertThrows(InValidAccessTokenException.class, () -> accessTokenGenerator.validateToken(invalidToken));
    }

    @Test
    public void testValidateTokenWithUserNotInCache() {
        when(userCache.getUser(2)).thenThrow(new UserDoesNotExistException("User does not exist"));
        UserResponseDTO user = new UserResponseDTO();
        user.setId(2);
        user.setUserName("testuser");
        user.setUserRole("ROLE_USER");
        String validToken = accessTokenGenerator.generateToken(user);
        assertThrows(InValidAccessTokenException.class, () -> accessTokenGenerator.validateToken(validToken));
    }
}