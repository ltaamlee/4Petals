package fourpetals.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/styles/**", "/css/**", "/js/**", 
						"/image/**", "/webjars/**").permitAll()
				.requestMatchers("/", "/home", "/register", "/login", 
						"/product", "/contact", "/about", 
						"/guest/**").permitAll()
				.requestMatchers("/admin/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/home", true)
				.permitAll()
			)
			.logout(logout -> logout
				.logoutSuccessUrl("/home")
				.permitAll()
			)
			.csrf(csrf -> csrf.disable());
		
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
	    UserDetails testUser = User.builder()
	            .username("testuser")
	            .password(passwordEncoder.encode("123456"))  // mật khẩu test
	            .roles("USER")
	            .build();
	    return new InMemoryUserDetailsManager(testUser);
	}
}