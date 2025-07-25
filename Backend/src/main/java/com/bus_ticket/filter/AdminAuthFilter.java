package com.bus_ticket.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // Allow access to login page, authentication endpoint, and public resources
        if (requestURI.equals("/") ||
            requestURI.equals("/admin/login") || 
            requestURI.equals("/admin/authenticate") ||
            requestURI.startsWith("/api/") ||
            requestURI.startsWith("/swagger-ui") ||
            requestURI.startsWith("/v3/api-docs") ||
            requestURI.startsWith("/static/") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/")) {
        chain.doFilter(request, response);
        return;
    }
    
    // Check if accessing admin pages
    if (requestURI.startsWith("/admin/")) {
        HttpSession session = httpRequest.getSession(false);
        
        if (session == null || session.getAttribute("adminLoggedIn") == null) {
            httpResponse.sendRedirect("/admin/login");
            return;
        }
    }
    
    chain.doFilter(request, response);
}
}
