package com.cineevent.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequestDTO {

	 private String userName;
	 private String password;
}
