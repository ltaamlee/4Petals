package fourpetals.com.config;

import fourpetals.com.security.CustomUserDetailsService;
import fourpetals.com.security.jwt.JwtAuthenticationEntryPoint;
import fourpetals.com.security.jwt.JwtAuthenticationFilter;
import fourpetals.com.security.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtTokenProvider tokenProvider;

	public SecurityConfig(CustomUserDetailsService customUserDetailsService,
			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenProvider tokenProvider) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.tokenProvider = tokenProvider;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
		return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/api/auth/login", "/api/auth/register", "/index", "/home", "/register",
								"/login", "/error", "/styles/**", "/css/**", "/js/**", "/images/**", "/webjars/**")
						.permitAll().requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(jwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
