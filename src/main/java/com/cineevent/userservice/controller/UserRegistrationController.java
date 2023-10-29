package com.cineevent.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineevent.userservice.dto.request.UserRequestDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.services.UserService;

@RestController
@RequestMapping
public class UserRegistrationController {

	@Autowired
	private UserService userService;

	/**
	 * Endpoint for registering a user
	 * @param userRequestDTO
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
		return new ResponseEntity<>(userService.createUser(userRequestDTO), null, HttpStatus.CREATED);
	}

}
