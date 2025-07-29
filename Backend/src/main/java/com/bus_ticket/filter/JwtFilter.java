package com.bus_ticket.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Injecting utility class that handles JWT validation
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Getting the request URI (endpoint path)
        String requestURI = request.getRequestURI();

        // Skip this filter for admin panel pages, vendor login/logout,
        // and static resources (like CSS/JS/images)
        if (requestURI.startsWith("/admin/") || 
            requestURI.startsWith("/vendor/") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/") ||
            requestURI.startsWith("/static/") ||
            requestURI.startsWith("/WEB-INF/")) {

            // Let the request continue without JWT checks
            filterChain.doFilter(request, response);
            return;
        }

        // Check if Authorization header is present and starts with "Bearer "
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Extract the JWT token from the header
            String token = authHeader.replace("Bearer ", "").trim();

            // Validate the token and get authentication object
            Authentication auth = jwtUtil.validateToken(token);

            // If token is valid and no auth is already set in context, set it
            if (auth != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
