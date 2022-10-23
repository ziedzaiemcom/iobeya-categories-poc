package com.iobeya.categories.poc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build();
		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").build();
		
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) ->  
				{
					try {
						requests
						//.antMatchers("/").permitAll()
						.antMatchers("/css/login.css").permitAll()
						.antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("USER", "ADMIN")
						.antMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("USER", "ADMIN")
						.antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
						.anyRequest().authenticated()
						.and().csrf().disable();
					} catch (Exception e) {
						e.printStackTrace();
					}
				})
			.formLogin((form) -> form.loginPage("/login").permitAll())
			.logout((logout) -> logout.permitAll());

		return http.build();
	}

}