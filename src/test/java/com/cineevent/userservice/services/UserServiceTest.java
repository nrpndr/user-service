package com.cineevent.userservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import com.cineevent.userservice.configuration.RabbitMQConfig;
import com.cineevent.userservice.controller.MessageController;
import com.cineevent.userservice.dto.request.UserRequestDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.entities.User;
import com.cineevent.userservice.exceptions.InValidUserInputException;
import com.cineevent.userservice.exceptions.InValidUserLoginInputException;
import com.cineevent.userservice.exceptions.UserDoesNotExistException;
import com.cineevent.userservice.messaging.MQMessageConsumer;

@SpringBootTest
@ComponentScan(basePackages = { "com.cineevent.userservice" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { RabbitMQConfig.class,
				MessageController.class, MQMessageConsumer.class }) })
@Sql({"classpath:test_schema.sql"})
public class UserServiceTest {

    @Autowired
    private UserService userService;
    
    @Test
    public void testCreateUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser1");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser1@example.com");
        userRequestDTO.setFirstName("John");

        UserResponseDTO userResponse = userService.createUser(userRequestDTO);

        assertNotNull(userResponse);
        assertEquals(userRequestDTO.getUserName(), userResponse.getUserName());
        assertEquals(userRequestDTO.getEmail(), userResponse.getEmail());
        assertEquals(userRequestDTO.getFirstName(), userResponse.getFirstName());
    }

    @Test
    public void testCreateUserWithInvalidInput() {
        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.createUser(null));
        
        String expectedMessage = "User data cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testCreateUserWithNullUsername() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName(null);
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser2@example.com");
        userRequestDTO.setFirstName("John");


        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.createUser(userRequestDTO));
        String expectedMessage = "username cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testCreateUserWithExistingUsername() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser2");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser2@example.com");
        userRequestDTO.setFirstName("John");
        
        userService.createUser(userRequestDTO);

        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.createUser(userRequestDTO));
        
        String expectedMessage = "This username testuser2 is already in use";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testCreateUserWithNullEmail() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser4");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail(null);
        userRequestDTO.setFirstName("John");


        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.createUser(userRequestDTO));
        
        String expectedMessage = "Email cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testCreateUserWithExistingEmail() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser3");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser3@example.com");
        userRequestDTO.setFirstName("John");
        
        userService.createUser(userRequestDTO);
        
        userRequestDTO.setUserName("testuser4");

        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.createUser(userRequestDTO));
        
        String expectedMessage = "This email testuser3@example.com is already in use";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testCreateUserWithNoFirstName() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser4");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser4@example.com");
        userRequestDTO.setFirstName("");

        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.createUser(userRequestDTO));
        
        String expectedMessage = "User firstName cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testGetUserByNullEmail() {
        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.getUserByEmail(null));
        
        String expectedMessage = "email cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    
    @Test
    public void testGetUserByValidEmail() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser5");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser5@example.com");
        userRequestDTO.setFirstName("John");
        
        userService.createUser(userRequestDTO);
        
        User user = userService.getUserByEmail("testuser5@example.com");
        
        if(user != null) {
        	assertTrue("testuser5@example.com".equals(user.getEmail()));
        }
    }
    
    @Test
    public void testGetUserById() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser6");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser6@example.com");
        userRequestDTO.setFirstName("John");
        
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        int userId = userResponseDTO.getId();
        
        UserResponseDTO userResponseDTOById = userService.getUserById(userId);

        assertNotNull(userResponseDTOById);
        assertEquals(userId, userResponseDTOById.getId());
    }

    @Test
    public void testGetUserByIdWithNonExistingUser() {
        int userId = 10001;
        Exception exception = assertThrows(UserDoesNotExistException.class, () -> userService.getUserById(userId));
        
        String expectedMessage = "User with id 10001 does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    	
    @Test
    public void testUpdateUser() {
    	
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser7");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser7@example.com");
        userRequestDTO.setFirstName("John");
        
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        
        int userId = userResponseDTO.getId();
        
        userRequestDTO.setUserName("testuser8");
        
        UserResponseDTO updatedUser = userService.updateUser(userId, userRequestDTO);

        assertNotNull(updatedUser);
        assertEquals(userRequestDTO.getUserName(), updatedUser.getUserName());
        
        userRequestDTO.setEmail("testuser8@example.com");
        
        updatedUser = userService.updateUser(userId, userRequestDTO);

        assertNotNull(updatedUser);
        assertEquals(userRequestDTO.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testUpdateUserWithNonExistingUserId() {
        int userId = 1001;
        UserRequestDTO userRequestDTO = null;

        Exception exception = assertThrows(UserDoesNotExistException.class, () -> userService.updateUser(userId, userRequestDTO));
        
        String expectedMessage = "User with id 1001 does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testUpdateUserWithInvalidInput() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser7");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser7@example.com");
        userRequestDTO.setFirstName("John");
        
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        
        int userId = userResponseDTO.getId();
    	
        UserRequestDTO userUpdateDTO = null;

        Exception exception = assertThrows(InValidUserInputException.class, () -> userService.updateUser(userId, userUpdateDTO));
        
        String expectedMessage = "User data cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testDeleteUserByIdOfExistingUser() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser9");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser9@example.com");
        userRequestDTO.setFirstName("John");
        
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        
        int userId = userResponseDTO.getId();
    	
        userService.deleteUserById(userId);

        Exception exception = assertThrows(UserDoesNotExistException.class, () -> userService.getUserById(userId));
        
        String expectedMessage = "User with id "+userId+" does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testDeleteUserByIdOfNonExistingUser() {
        int userId = 1001;
    	
        Exception exception = assertThrows(UserDoesNotExistException.class, () ->  userService.deleteUserById(userId));
        
        String expectedMessage = "User with id "+userId+" does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testGetAllUsers() {
        List<UserResponseDTO> allUsers = userService.getAllUsers();
        for(UserResponseDTO user : allUsers) {
        	assertNotNull(user.getId());
        	assertNotNull(user.getFirstName());
        	assertNotNull(user.getUserName());
        	assertNotNull(user.getEmail());
        }
    }
    
    @Test
    public void testLoginWithValidCredentials() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser10");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser10@example.com");
        userRequestDTO.setFirstName("John");
        
        userService.createUser(userRequestDTO);
        
        String userName = userRequestDTO.getUserName();
        String password = userRequestDTO.getPassword();

        UserResponseDTO userLoginResponseDTO = userService.login(userName, password);

        assertNotNull(userLoginResponseDTO);
        assertEquals(userName, userLoginResponseDTO.getUserName());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser11");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser11@example.com");
        userRequestDTO.setFirstName("John");
        
        userService.createUser(userRequestDTO);
        
        String userName = userRequestDTO.getUserName();
        String password = userRequestDTO.getPassword()+"invalid";

        Exception exception = assertThrows(InValidUserLoginInputException.class, () -> userService.login(userName, password));
        
        String expectedMessage = "Invalid username or password";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    public void testLoginWithNonExistingUser() {
    	UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("testuser12");
        userRequestDTO.setPassword("password@123");
        userRequestDTO.setEmail("testuser12@example.com");
        userRequestDTO.setFirstName("John");
        
        userService.createUser(userRequestDTO);
        
        String userName = userRequestDTO.getUserName()+"invalid";
        String password = userRequestDTO.getPassword();

        Exception exception = assertThrows(InValidUserLoginInputException.class, () -> userService.login(userName, password));
        
        String expectedMessage = "Invalid username or password";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testLoginWithNullCredentials() {
    	Exception exception = assertThrows(InValidUserLoginInputException.class, () -> userService.login(null, null));
        String expectedMessage = "userName or password cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testLoginWithNullUsername() {
        Exception exception = assertThrows(InValidUserLoginInputException.class, () -> userService.login(null, "password"));
        String expectedMessage = "userName or password cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testLoginWithNullPassword() {
        Exception exception = assertThrows(InValidUserLoginInputException.class, () -> userService.login("username", null));
        String expectedMessage = "userName or password cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

}
