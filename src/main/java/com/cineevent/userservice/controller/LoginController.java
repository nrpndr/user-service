package com.cineevent.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineevent.userservice.dto.request.LoginRequestDTO;
import com.cineevent.userservice.dto.response.LoginResponseDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.security.AccessTokenGenerator;
import com.cineevent.userservice.services.UserService;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AccessTokenGenerator tokenGenerator;
    
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
    	LoginResponseDTO response = new LoginResponseDTO();
    	UserResponseDTO userResponseDTO = userService.login(loginRequestDTO.getUserName(), loginRequestDTO.getPassword());
    	String accessToken = tokenGenerator.generateToken(userResponseDTO);
    	response.setAccessToken(accessToken);
    	response.setMessage("Login Successful");
    	return response;
    }

}
