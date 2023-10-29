package com.cineevent.userservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAuthResponseDTO {

	private int userId;

	private String userName;
	
	private String userRole;
}
