package com.cineevent.userservice.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cineevent.userservice.dto.response.UserAuthResponseDTO;
import com.cineevent.userservice.exceptions.InValidAccessTokenException;
import com.cineevent.userservice.security.ThreadLocalAuthStore;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component("authorizationFilter")
@Log4j2
public class AuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("In AuthorizationFilter and Request is:: " + request.getRequestURI());

		UserAuthResponseDTO authResponseDTO = ThreadLocalAuthStore.getAuthDetails();
		log.info("authResponseDTO="+authResponseDTO);
		if(authResponseDTO == null) {
			throw new InValidAccessTokenException("Authorization header value is not correct");
		}
		final List<String> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(authResponseDTO.getUserRole());

		final List<GrantedAuthority> authorities = new ArrayList<>();
		if (grantedAuthorities != null) {
			for (final String grantedAuthority : grantedAuthorities) {
				authorities.add(new SimpleGrantedAuthority(grantedAuthority));
			}
		}

		loadCurrentAuthorities(authorities);
		
		final UsernamePasswordAuthenticationToken upAuthentication = new UsernamePasswordAuthenticationToken(authResponseDTO, null,
				authorities);
		SecurityContextHolder.getContext().setAuthentication(upAuthentication);
		UserAuthResponseDTO authResponseDTO2 = ThreadLocalAuthStore.getAuthDetails();
		log.info("authResponseDTO2="+authResponseDTO2);
		log.info("authResponseDTO2="+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		filterChain.doFilter(request, response);

	}

	private void loadCurrentAuthorities(List<GrantedAuthority> authorities) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (!CollectionUtils.isEmpty(authentication.getAuthorities())) {
				log.info("Found existing authorities, so adding them");
				authorities.addAll(authentication.getAuthorities());
			}
		}
	}

}
