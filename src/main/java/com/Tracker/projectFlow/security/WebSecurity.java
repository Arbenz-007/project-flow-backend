package com.Tracker.projectFlow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Tracker.projectFlow.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration authConfiguration) {
		return authConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		

	    http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults());

	    http.sessionManagement(session ->
	            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    http.authenticationProvider(authenticationProvider());

	    http.authorizeHttpRequests(auth -> auth
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .requestMatchers("/api/auth/**").permitAll()
	            .anyRequest().authenticated()
	    );

	    http.addFilterBefore(jwtAuthenticationFilter,
	            UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider(userDetailsServiceImpl);
		daoAuth.setPasswordEncoder(passwordEncoder());

		return daoAuth;
	}
}
