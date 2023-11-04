package com.cineevent.userservice.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cineevent.userservice.cache.UserCache;
import com.cineevent.userservice.dto.response.UserAuthResponseDTO;
import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.exceptions.AccessTokenExpiredException;
import com.cineevent.userservice.exceptions.InValidAccessTokenException;
import com.cineevent.userservice.exceptions.UserDoesNotExistException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AccessTokenGenerator {
	
	private static final String SECRET_FOR_TOKEN = "USER_SERVICE_SECRET_NRPNDR";
	private static final long TOKEN_VALID_DURATION_IN_MILLI_SEC = 60*60000l;
	
	@Autowired
	private UserCache userCache;
	
	public String generateToken(UserResponseDTO user) {
		String jwtToken = "";
		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("userId", user.getId());
		claimsMap.put("userRole", user.getUserRole());
		long currentTime = System.currentTimeMillis();
		jwtToken = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(new Date(currentTime)).addClaims(claimsMap).setExpiration(new Date(currentTime+TOKEN_VALID_DURATION_IN_MILLI_SEC))
				.signWith(SignatureAlgorithm.HS256, SECRET_FOR_TOKEN).compact();
		return jwtToken;
	}
	
	public UserAuthResponseDTO validateToken(String accessToken) throws AccessTokenExpiredException, InValidAccessTokenException{
		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET_FOR_TOKEN).parseClaimsJws(accessToken).getBody();
			int userIdInToken = (Integer) claims.get("userId");
			String userRoleInToken = (String) claims.get("userRole");
			String userNameInToken = (String) claims.get("sub");
			
			userCache.getUser(userIdInToken);
			
			UserAuthResponseDTO authResponseDTO = new UserAuthResponseDTO();
			authResponseDTO.setUserId(userIdInToken);
			authResponseDTO.setUserName(userNameInToken);
			authResponseDTO.setUserRole(userRoleInToken);
			
			return authResponseDTO;
		} catch (ExpiredJwtException expiredJwtException) {
			log.error("Accesstoken expired, error message {}", expiredJwtException.getMessage());
			throw new AccessTokenExpiredException("Accesstoken is expired");
		} catch (UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException invalidTokenException) {
			log.error("Accesstoken expired", invalidTokenException);
			throw new InValidAccessTokenException("InValid  access token");
		} catch (UserDoesNotExistException userDoesNotExistException) {
			log.error("User  does not exist", userDoesNotExistException);
			throw new InValidAccessTokenException("InValid  access token");
		}
	}
}
