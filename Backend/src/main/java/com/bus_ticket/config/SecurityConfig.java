package com.bus_ticket.config;

import com.bus_ticket.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injecting the JWT filter which we'll apply to API requests
    @Autowired
    private JwtFilter jwtFilter;

    // Bean to encode passwords using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Main security configuration method
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Enabling CORS
            .cors().and()

            // Disabling CSRF since we're using tokens
            .csrf().disable()

            // Defining route-based access control
            .authorizeHttpRequests(auth -> auth

                // Admin JSP pages can be accessed without login, handled separately
                .requestMatchers("/admin/**").permitAll()

                // Allowing vendor login/logout related URLs publicly
                .requestMatchers("/vendor/login", "/vendor/authenticate", "/vendor/logout").permitAll()

                // Allowing access to static files like CSS, JS, images, etc.
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                .requestMatchers("/WEB-INF/**").permitAll()

                // Public APIs that don’t need authentication
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                .requestMatchers("/api/vendor/login").permitAll()
                .requestMatchers("/api/buses/search").permitAll()
                .requestMatchers("/api/buses").permitAll() // list of all buses
                .requestMatchers("/api/schedules/search").permitAll()
                .requestMatchers("/api/cities/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // Vendor-specific APIs require VENDOR role
                .requestMatchers("/api/buses/vendor/**").hasRole("VENDOR")
                .requestMatchers("/api/schedules/vendor/**").hasRole("VENDOR")
                .requestMatchers("/api/schedules").hasRole("VENDOR")
                .requestMatchers("/api/schedules/**").hasRole("VENDOR")
                .requestMatchers("/api/vendor/**").hasRole("VENDOR")

                // User-related APIs require USER role
                .requestMatchers("/api/users/**").hasRole("USER")

                // Admin APIs require ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Any other request needs to be authenticated
                .anyRequest().authenticated()
            )

            // Session management setup
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Allow sessions for admin panel
                .maximumSessions(1) // One session at a time
                .maxSessionsPreventsLogin(false) // Allow new login by kicking old session
            );

        // Adding the JWT filter before UsernamePasswordAuthenticationFilter for API security
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS configuration bean to allow frontend apps to communicate
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allowing requests from React dev servers
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));

        // Typical REST methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers
        config.setAllowedHeaders(List.of("*"));

        // Allow cookies (important for sessions and JWT headers)
        config.setAllowCredentials(true);

        // Register this CORS config for all URLs
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
