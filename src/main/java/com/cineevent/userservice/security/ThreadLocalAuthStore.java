package com.cineevent.userservice.security;

import com.cineevent.userservice.dto.response.UserAuthResponseDTO;

public final class ThreadLocalAuthStore {
	
	private ThreadLocalAuthStore() {
		throw new IllegalStateException("Util Class");
	}

	public static final ThreadLocal<String> accessTokenThreadLocal = new ThreadLocal<>();

	public static final ThreadLocal<UserAuthResponseDTO> threadLocalAuthDetails = new ThreadLocal<>();

	public static String getAccessToken() {
		return accessTokenThreadLocal.get();
	}

	public static void setAccessToken(String appToken) {
		accessTokenThreadLocal.set(appToken);
	}
	
	public static UserAuthResponseDTO getAuthDetails() {
		return threadLocalAuthDetails.get();
	}

	public static void setAuthDetails(UserAuthResponseDTO authResponse) {
		threadLocalAuthDetails.set(authResponse);
	}
}
