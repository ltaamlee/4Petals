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



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtTokenProvider tokenProvider) {
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
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/index", "/home", "/register", "/login", "/logout",
                    "/product", "/about", "/contact",
                    "/styles/**", "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/api/auth/login", "/api/auth/register"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(logout -> logout.disable());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

