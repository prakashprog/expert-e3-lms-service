package com.expertworks.lms.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expertworks.lms.service.CustomUserDetailsService;
import com.expertworks.lms.util.AuthTokenDetails;
import com.expertworks.lms.util.JwtUtil;
//import com.javatechie.jwt.api.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService service;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		String token = null;
		String userName = null;
		AuthTokenDetails authTokenDetails = null;

		try {

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtUtil.extractUsername(token);

				Claims claims = jwtUtil.extractAllClaims(token);

				// System.out.println(claims.get("PartnerId", String.class));

				authTokenDetails = new AuthTokenDetails();
				authTokenDetails.setPartnerId(claims.get("PartnerId", String.class));
				authTokenDetails.setTeamId(claims.get("TeamId", String.class));
			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = service.loadUserByUsername(userName);

				if (jwtUtil.validateToken(token, userDetails)) {
					
					System.out.println("Validation token is not expired");

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, authTokenDetails, userDetails.getAuthorities());

					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

				}
				
				
			}
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			System.out.println("Exception occured in Filter : "+e.getMessage());
			e.printStackTrace();

			((HttpServletResponse) httpServletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			//((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

		}
	} // try close
}