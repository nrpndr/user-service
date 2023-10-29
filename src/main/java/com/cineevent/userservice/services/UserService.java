package com.cineevent.userservice.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cineevent.userservice.dto.request.UserRequestDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.entities.User;
import com.cineevent.userservice.exceptions.InValidUserInputException;
import com.cineevent.userservice.exceptions.InValidUserLoginInputException;
import com.cineevent.userservice.exceptions.UserDoesNotExistException;
import com.cineevent.userservice.repositories.UserRepository;
import com.cineevent.userservice.role.UserRole;
import com.cineevent.userservice.security.PasswordEncryptor;
import com.cineevent.userservice.transformer.UserDTOTransformer;
import com.cineevent.userservice.validators.PasswordValidator;
import com.cineevent.userservice.validators.UserEmailValidator;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordValidator passwordValidator;
	
	@Autowired
	private UserEmailValidator userEmailValidator;

	@Transactional
	public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
		
		if (userRequestDTO == null) {
            throw new InValidUserInputException("User data cannot be null");
        }
		
		checkIfUsernameNotUsed(userRequestDTO.getUserName());
	    passwordValidator.checkPassword(userRequestDTO.getPassword());
	    userEmailValidator.checkEmail(userRequestDTO.getEmail());
	    checkIfEmailNotUsed(userRequestDTO.getEmail());
	    
	    if (Strings.isBlank(userRequestDTO.getFirstName())) {
            throw new InValidUserInputException("User firstName cannot be null");
        }
	    
		
		User user = UserDTOTransformer.transformToUser(userRequestDTO);
		//setting userRole as USER for every user getting created
		user.setUserRole(UserRole.USER.name());
		userRepository.save(user);
		return UserDTOTransformer.transformToUserResponseDTO(user);
	}

	/**
	 * Check if the username is already belonging to some other user
	 * @param username
	 */
	private void checkIfUsernameNotUsed(String username) {
		User userByUsername = getUserByUsername(username);
		if (userByUsername != null) {
			String msg = String.format("The username %s is already in use from another user with ID = %s",
					userByUsername.getUserName(), userByUsername.getId());
			log.error(msg);
			throw new InValidUserInputException(String.format("This username %s is already in use.",
					username));
		}
	}
    
	@Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        if (Strings.isBlank(username)) {
            throw new InValidUserInputException("username cannot be null or empty");
        }
        return userRepository.findByUserName(username);
    }

    /**
     * Check if the email is already belonging to some other user
     * @param email
     */
    public void checkIfEmailNotUsed(String email) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail != null) {
        	String msg = String.format("The email %s is already in use from another user with ID = %s",
                    userByEmail.getEmail(), userByEmail.getId());
			log.error(msg);
			throw new InValidUserInputException(String.format("This email %s is already in use.",
					email));
        }
    }
    
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        if (Strings.isBlank(email)) {
            throw new InValidUserInputException("email cannot be null");
        }
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
	public UserResponseDTO getUserById(int id) {
		User user = userRepository.findById(id).orElse(null);
		if(user != null) {
			return UserDTOTransformer.transformToUserResponseDTO(user);
		}
		throw constructUserDoesNotExistException("id", String.valueOf(id));
	}

    @Transactional
	public UserResponseDTO updateUser(int id, UserRequestDTO userRequestDTO) {
		if (userRequestDTO == null) {
            throw new InValidUserInputException("User data cannot be null");
        }
		
		if(Strings.isNotBlank(userRequestDTO.getUserName())) {
			checkIfUsernameNotUsed(userRequestDTO.getUserName());
		}
		
		if(Strings.isNotBlank(userRequestDTO.getPassword())) {
			passwordValidator.checkPassword(userRequestDTO.getPassword());
		}
		
		if(Strings.isNotBlank(userRequestDTO.getEmail())) {
			 userEmailValidator.checkEmail(userRequestDTO.getEmail());
			 checkIfEmailNotUsed(userRequestDTO.getEmail());
		}
	    
		User userFromDB = userRepository.findById(id).orElse(null);
		
		if(userFromDB == null) {
			throw constructUserDoesNotExistException("id", String.valueOf(id));
		}
		
		UserDTOTransformer.updateUserFromDB(userFromDB, userRequestDTO);
		userRepository.save(userFromDB);
		return UserDTOTransformer.transformToUserResponseDTO(userFromDB);
	}
	
	@Transactional
	public void deleteUserById(int id) {

		User userFromDB = userRepository.findById(id).orElse(null);
		if (userFromDB == null) {
			throw constructUserDoesNotExistException("id", String.valueOf(id));
		}

		userRepository.deleteById(id);
		log.info(String.format("User with id %s has been deleted.", id));
	}
	
	private UserDoesNotExistException constructUserDoesNotExistException(String fieldName, String fieldValue) {
		String msg = String.format("User with %s %s does not exist", fieldName, fieldValue);
		return new UserDoesNotExistException(msg);
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> getAllUsers() {
		List<User> usersFromDB = userRepository.findAll();
		log.info("No of users from db are {}", usersFromDB.size());
		List<UserResponseDTO> users = new ArrayList<>();
		if(!CollectionUtils.isEmpty(usersFromDB)) {
			for(User user : usersFromDB) {
				UserResponseDTO userFromDB = UserDTOTransformer.transformToUserResponseDTO(user);
				users.add(userFromDB);
			}
		}
		return users;
	}

	@Transactional(readOnly = true)
	public UserResponseDTO login(String userName, String password) {
		if ((Strings.isBlank(userName)) || (Strings.isBlank(password))) {
            throw new InValidUserLoginInputException("userName or password cannot be null or empty");
        }

        User user = getUserByUsername(userName);
        if (user == null) {
            log.warn("InValid login attempt with username {}, no such user exist", userName);
            throw new InValidUserLoginInputException("Invalid username or password");
        }

        log.info("Login request with username {}", userName);

        // check the password
        if (PasswordEncryptor.isPasswordValid(password, user.getPassword())) {
            log.info("Valid login for user {} at time {}", userName, System.currentTimeMillis());
        } else {
        	log.warn("InValid login attempt with username {}, wrong password attempt", userName);
            throw new InValidUserLoginInputException("Invalid username or password");
        }
        return UserDTOTransformer.transformToUserResponseDTO(user);
	}

}
