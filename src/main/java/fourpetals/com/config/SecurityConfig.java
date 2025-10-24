package fourpetals.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
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

    // 🔹 Cấu hình role kế thừa
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("""
            ROLE_ADMIN > ROLE_MANAGER
            ROLE_MANAGER > ROLE_SALES_EMPLOYEE
            ROLE_SALES_EMPLOYEE > ROLE_SHIPPER
        """);
        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/web/**", "/api/**", "/api/auth/register", "/api/auth/login/", 
                                 "/index", "/home", "/register", "/login", "/logout", "/product/**", 
                                 "/about", "/contact", "/error", "/styles/**", "/css/**", "/js/**", 
                                 "/images/**", "/image/**", "/webjars/**", "/forgot-password/**", 
                                 "/verify-otp/**", "/api/payment/momo/callback")
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/inventory/**").hasAnyRole("INVENTORY_EMPLOYEE", "MANAGER", "ADMIN")
                .requestMatchers("/sales/**").hasAnyRole("SALES_EMPLOYEE", "MANAGER", "ADMIN")
                .requestMatchers("/shipper/**").hasAnyRole("SHIPPER", "MANAGER", "ADMIN")
                .anyRequest().authenticated()
            )
            // 🔹 Xử lý khi bị lỗi quyền hoặc chưa đăng nhập
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, ex1) -> res.sendRedirect("/401"))
                .accessDeniedHandler((req, res, ex2) -> res.sendRedirect("/403"))
            )
            // 🔹 Stateless session cho JWT
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(logout -> logout.disable());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
