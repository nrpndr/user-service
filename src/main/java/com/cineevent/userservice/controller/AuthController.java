package com.cineevent.userservice.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineevent.userservice.dto.request.TokenDTO;
import com.cineevent.userservice.dto.response.UserAuthResponseDTO;
import com.cineevent.userservice.exceptions.InValidTokenInputException;
import com.cineevent.userservice.security.AccessTokenGenerator;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private AccessTokenGenerator tokenGenerator;
    
    @PostMapping("/validateToken")
    public UserAuthResponseDTO validateToken(@RequestBody TokenDTO tokenDTO) {
    	if(tokenDTO == null) {
    		throw new InValidTokenInputException("Missing request body for the request");
    	}
    	
    	if(StringUtils.isEmpty(tokenDTO.getAccessToken())) {
    		throw new InValidTokenInputException("Missing accessToken in the request body");
    	}
    	
    	return tokenGenerator.validateToken(tokenDTO.getAccessToken());
    }
}
