package com.cineevent.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineevent.userservice.cache.UserCache;
import com.cineevent.userservice.dto.request.UserMQMessage;
import com.cineevent.userservice.dto.request.UserRequestDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.services.RabbitMQProducer;
import com.cineevent.userservice.services.UserService;
import com.google.gson.Gson;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserCache userCache;
	
	@Autowired
	private RabbitMQProducer rabbitMQProducer;

	/**
	 * Endpoint for creating a user
	 * Only ADMIN users can create a new user
	 * @param userRequestDTO
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
		return new ResponseEntity<>(userService.createUser(userRequestDTO), null, HttpStatus.CREATED);
	}

	/**
	 * Endpoint for getting all the users
	 * Only ADMIN users can get all the users
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	public List<UserResponseDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	/**
	 * Endpoint for getting user by id
	 * Only ADMIN users can get any user details, normal USER can access only their own user details
	 * @param id
	 * @return
	 */
	@GetMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN') || #userId == principal.userId")
	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	public UserResponseDTO getUserById(@PathVariable("userId") int userId) {
		return userCache.getUser(userId);
	}

	/**
	 * Endpoint for updating user by id
	 * Only ADMIN users can update any user details, normal USER can update only their own user details
	 * @param id
	 * @param userRequestDTO
	 * @return
	 */
	@PatchMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN') || #userId == principal.userId")
	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	public UserResponseDTO updateUser(@PathVariable("userId") int userId, @RequestBody UserRequestDTO userRequestDTO) {
		return userService.updateUser(userId, userRequestDTO);
	}

	/**
	 * Endpoint for deleting user by id
	 * Only ADMIN users can delete any user details,
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	public ResponseEntity<?> deleteById(@PathVariable("userId") int id) {
		userService.deleteUserById(id);
		UserMQMessage userMQMessage = new UserMQMessage("DELETE", id);
		rabbitMQProducer.sendMessage(new Gson().toJson(userMQMessage));
		return ResponseEntity.noContent().build();
	}

}
