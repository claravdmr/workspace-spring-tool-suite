package com.example.mechanical_workshop.infrastructure.apirest.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		final CorsConfiguration cc = new CorsConfiguration();
		cc.addAllowedOrigin("*");
		cc.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
		cc.setAllowedHeaders(Collections.singletonList("*"));
		return http.cors(cors -> new CorsConfiguration().combine(cc)).csrf(csrf -> csrf.disable()).build();
	}

}