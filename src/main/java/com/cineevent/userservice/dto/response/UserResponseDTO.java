package com.cineevent.userservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDTO {
	
	private int id;

	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String userRole;
}
