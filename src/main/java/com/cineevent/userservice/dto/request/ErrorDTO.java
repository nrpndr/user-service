package com.cineevent.userservice.dto.request;

import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ErrorDTO {
	
	private String message;
	private int statusCode;

}
