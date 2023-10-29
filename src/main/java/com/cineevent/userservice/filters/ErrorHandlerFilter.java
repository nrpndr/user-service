package com.cineevent.userservice.filters;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cineevent.userservice.dto.request.ErrorDTO;
import com.cineevent.userservice.dto.response.ErrorResponseDTO;
import com.cineevent.userservice.exceptions.GenericExceptionHandler;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("errorHandlerFilter")
public class ErrorHandlerFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			ErrorDTO errorDTO = GenericExceptionHandler.handleException(e);
			handleError(request, response, errorDTO.getMessage(), errorDTO.getStatusCode());
		}
	}
	
	private void handleError(HttpServletRequest request, HttpServletResponse response, String message,
			Integer statusCode) throws IOException {
		ErrorResponseDTO respObj = new ErrorResponseDTO(message);
		response.setStatus(statusCode);
		response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
		final PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(respObj));
		out.flush();
	}

}
