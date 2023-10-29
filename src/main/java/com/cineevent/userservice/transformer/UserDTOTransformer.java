package com.cineevent.userservice.transformer;

import org.apache.logging.log4j.util.Strings;

import com.cineevent.userservice.dto.request.UserRequestDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.entities.User;
import com.cineevent.userservice.security.PasswordEncryptor;

public final class UserDTOTransformer {
	
	private UserDTOTransformer() {
		 throw new IllegalStateException("Tranformer class");
	}
	
	public static User transformToUser(UserRequestDTO userRequestDTO) {
		User user = new User();
		
		user.setEmail(userRequestDTO.getEmail());
		user.setFirstName(userRequestDTO.getFirstName());
		user.setLastName(userRequestDTO.getLastName());
		user.setUserName(userRequestDTO.getUserName());
		user.setPassword(PasswordEncryptor.getEncryptedPassword(userRequestDTO.getPassword()));
		
		return user;
	}
	
	public static UserResponseDTO transformToUserResponseDTO(User user) {
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		
		userResponseDTO.setId(user.getId());
		userResponseDTO.setEmail(user.getEmail());
		userResponseDTO.setFirstName(user.getFirstName());
		userResponseDTO.setLastName(user.getLastName());
		userResponseDTO.setUserName(user.getUserName());
		userResponseDTO.setUserRole(user.getUserRole());
		
		return userResponseDTO;
	}

	public static void updateUserFromDB(User userFromDB, UserRequestDTO userRequestDTO) {

		if (Strings.isNotBlank(userRequestDTO.getEmail())) {
			userFromDB.setEmail(userRequestDTO.getEmail());
		}

		if (Strings.isNotBlank(userRequestDTO.getFirstName())) {
			userFromDB.setFirstName(userRequestDTO.getFirstName());
		}

		if (Strings.isNotBlank(userRequestDTO.getLastName())) {
			userFromDB.setLastName(userRequestDTO.getLastName());
		}

		if (Strings.isNotBlank(userRequestDTO.getUserName())) {
			userFromDB.setUserName(userRequestDTO.getUserName());
		}

		if (Strings.isNotBlank(userRequestDTO.getPassword())) {
			userFromDB.setPassword(PasswordEncryptor.getEncryptedPassword(userRequestDTO.getPassword()));
		}

	}

}
