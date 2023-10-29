package com.cineevent.userservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponseDTO {
	
	private String accessToken;
	private String message;

}
