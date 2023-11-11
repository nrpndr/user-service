package com.cineevent.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO Used for user create/update
 * @author NripendraThakur
 *
 */
@Getter
@Setter
@ToString
public class UserRequestDTO {

	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;

}
