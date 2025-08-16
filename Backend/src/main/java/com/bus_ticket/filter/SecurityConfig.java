package com.bus_ticket.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

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

                // Admin JSP pages can be accessed without login, handled separately by AdminAuthFilter
                .requestMatchers("/admin/**").permitAll()

                // Allowing vendor login/logout related URLs publicly
                .requestMatchers("/vendor/login", "/vendor/authenticate", "/vendor/logout").permitAll()

                // Allowing access to static files like CSS, JS, images, etc.
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                .requestMatchers("/WEB-INF/**").permitAll()

                // Public APIs that don't need authentication
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                .requestMatchers("/api/users/id/**", "/api/users/{userId}").permitAll() // Allow user profile access
                .requestMatchers("/api/users/{userId}/deactivate").permitAll() // Allow deactivation
                .requestMatchers("/api/vendor/login", "/api/vendor/register").permitAll()
                .requestMatchers("/api/vendor/{vendorId}/deactivate", "/api/vendor/{vendorId}/profile", "/api/vendor/{vendorId}/change-password").permitAll() // Allow vendor deactivation, profile update, and password change
                .requestMatchers("/api/admin/**").permitAll() // Allow admin APIs for admin panel
                .requestMatchers("/api/buses/search").permitAll() // Allow bus search operations
                .requestMatchers("/api/schedules/search").permitAll()
                .requestMatchers("/api/schedules/{id}").permitAll() // Allow getting schedule by ID
                .requestMatchers("/api/cities/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/api/seats/**").permitAll() // Allow seat-related APIs
                .requestMatchers("/api/bookings/**").permitAll() // Allow booking-related APIs
                .requestMatchers("/api/payments/**").permitAll() // Allow payment APIs
                .requestMatchers("/api/feedback/**").permitAll() // Allow feedback APIs
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // Vendor-specific APIs require VENDOR role
                .requestMatchers("/api/buses/vendor/**").hasRole("VENDOR")
                .requestMatchers("/api/buses/**").hasRole("VENDOR") // Protect all bus operations (add, delete, etc.)
                .requestMatchers("/api/schedules/vendor/**").hasRole("VENDOR")
                .requestMatchers("/api/schedules").hasRole("VENDOR")
                .requestMatchers("/api/schedules/**").hasRole("VENDOR")
                .requestMatchers("/api/vendor/**").hasRole("VENDOR")

                // User-related APIs require USER role
                .requestMatchers("/api/users/**").hasRole("USER")

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

    // Admin Authentication Filter Component
    // This filter runs for any URL that starts with /admin/*
    @Component
    @WebFilter(urlPatterns = "/admin/*")
    @Order(1)
    public static class AdminAuthFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            // Typecasting the request and response to HTTP-specific versions
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            // Get the full URI of the incoming request
            String requestURI = httpRequest.getRequestURI();

            // Allow access to specific pages even without login
            // These include the home page, login, authentication, logout, API, Swagger docs, and static resources
            if (requestURI.equals("/") ||
                requestURI.equals("/admin/") ||
                requestURI.equals("/admin/login") ||
                requestURI.equals("/admin/authenticate") ||
                requestURI.equals("/admin/logout") ||
                requestURI.startsWith("/api/") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/static/") ||
                requestURI.startsWith("/css/") ||
                requestURI.startsWith("/js/") ||
                requestURI.startsWith("/images/")) {

                // Let the request pass through to the next filter or controller
                chain.doFilter(request, response);
                return;
            }

            // Now we check for access to protected /admin/ pages (anything else not matched above)
            if (requestURI.startsWith("/admin/")) {
                // Get the session if it exists
                HttpSession session = httpRequest.getSession(false);

                // If no session or session does not have 'adminLoggedIn' attribute, redirect to login
                if (session == null || session.getAttribute("adminLoggedIn") == null) {
                    httpResponse.sendRedirect("/admin/login");
                    return;
                }
            }

            // Everything is okay, let the request move forward
            chain.doFilter(request, response);
        }
    }
}
