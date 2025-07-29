package com.bus_ticket.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

// This filter runs for any URL that starts with /admin/*
@Component
@WebFilter(urlPatterns = "/admin/*")
@Order(1)
public class AdminAuthFilter implements Filter {

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
