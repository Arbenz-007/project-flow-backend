package com.Tracker.projectFlow.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Tracker.projectFlow.service.UserDetailsServiceImpl;
import com.Tracker.projectFlow.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtTokenProvider;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			try {
				//Get JWT Header
				
				String jwt=jwtTokenProvider.getJwtFromHeader(request);
				
				if(jwt!=null && jwtTokenProvider.validateToken(jwt)) {
					String username=jwtTokenProvider.getUserNameFromJwtToken(jwt);
					UserDetails  userDetails= userDetailsService.loadUserByUsername(username);
					
					if(userDetails != null) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
						
					}
				}
				//Validate Token
				
				// If Valid get user details
				//--> get user name -> load user -> set authentication context
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			filterChain.doFilter(request, response);
		}

		
	}


