package fourpetals.com.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import fourpetals.com.security.CustomUserDetailsService;
import fourpetals.com.security.jwt.JwtAuthenticationEntryPoint;
import fourpetals.com.security.jwt.JwtAuthenticationFilter;
import fourpetals.com.security.jwt.JwtTokenProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) 
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
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/ws-chat/**", "/app/**", "/topic/**").permitAll()
						.requestMatchers("/manager/**").hasRole("MANAGER").requestMatchers("/shipper/**")
						.hasRole("SHIPPER")
						.requestMatchers("/", "/web/**", "/api/**", "/api/auth/register", "/api/auth/login/", "/index",
								"/home", "/register", "/login", "/logout", "/product/**", "/about", "/contact",
								"/error", "/styles/**", "/css/**", "/js/**", "/images/**", "/image/**", "/webjars/**",
								"/inventory/**", "/shipper/**", "/forgot-password/**", "/verify-otp/**")
						.permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/manager/**").hasRole("MANAGER")
						.requestMatchers("/inventory/**").hasRole("INVENTORY_EMPLOYEE")
						.requestMatchers("/shipper/**").hasRole("SHIPPER")
						.requestMatchers("/sales/**").hasRole("SALES_EMPLOYEE")
						.requestMatchers("/api/payment/momo/callback").permitAll()
						.anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.logout(logout -> logout.disable());

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
