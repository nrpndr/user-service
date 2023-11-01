package com.cineevent.userservice.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import com.cineevent.userservice.dto.request.UserRequestDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.entities.User;

public class UserDTOTransformerTest {

    @Test
    public void testTransformToUser() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("user@example.com");
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setUserName("johndoe");
        requestDTO.setPassword("password123");

        User user = UserDTOTransformer.transformToUser(requestDTO);

        assertEquals(requestDTO.getEmail(), user.getEmail());
        assertEquals(requestDTO.getFirstName(), user.getFirstName());
        assertEquals(requestDTO.getLastName(), user.getLastName());
        assertEquals(requestDTO.getUserName(), user.getUserName());
        // Ensure the password is encrypted
        assertNotNull(user.getPassword());
        assertNotEquals(requestDTO.getPassword(), user.getPassword());
    }

    @Test
    public void testTransformToUserResponseDTO() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setUserRole("user");

        UserResponseDTO responseDTO = UserDTOTransformer.transformToUserResponseDTO(user);

        assertEquals(user.getId(), responseDTO.getId());
        assertEquals(user.getEmail(), responseDTO.getEmail());
        assertEquals(user.getFirstName(), responseDTO.getFirstName());
        assertEquals(user.getLastName(), responseDTO.getLastName());
        assertEquals(user.getUserName(), responseDTO.getUserName());
        assertEquals(user.getUserRole(), responseDTO.getUserRole());
    }

    @Test
    public void testUpdateUserFromDB() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("newemail@example.com");
        requestDTO.setFirstName("Jane");
        requestDTO.setLastName("Smith");
        requestDTO.setUserName("janesmith");
        requestDTO.setPassword("newpassword123");

        User userFromDB = new User();
        userFromDB.setId(1);
        userFromDB.setEmail("user@example.com");
        userFromDB.setFirstName("John");
        userFromDB.setLastName("Doe");
        userFromDB.setUserName("johndoe");
        userFromDB.setUserRole("user");

        UserDTOTransformer.updateUserFromDB(userFromDB, requestDTO);

        assertEquals(requestDTO.getEmail(), userFromDB.getEmail());
        assertEquals(requestDTO.getFirstName(), userFromDB.getFirstName());
        assertEquals(requestDTO.getLastName(), userFromDB.getLastName());
        assertEquals(requestDTO.getUserName(), userFromDB.getUserName());
        // Ensure the password is updated and encrypted
        assertNotNull(userFromDB.getPassword());
        assertNotEquals(requestDTO.getPassword(), userFromDB.getPassword());
        assertEquals(requestDTO.getUserName(), userFromDB.getUserName());
    }
}
