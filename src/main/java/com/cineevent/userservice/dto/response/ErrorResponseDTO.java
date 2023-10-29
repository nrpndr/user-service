package com.cineevent.userservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponseDTO {

	private final String message;
	private final long timestamp;

	public ErrorResponseDTO(final String message) {
		this.timestamp = System.currentTimeMillis();
		this.message = message;
	}
}
