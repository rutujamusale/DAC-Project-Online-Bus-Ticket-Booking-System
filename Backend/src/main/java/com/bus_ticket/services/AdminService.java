package com.bus_ticket.services;

import com.bus_ticket.dto.AdminLoginRequest;
import com.bus_ticket.dto.ApiResponse;

public interface AdminService {
    
    ApiResponse authenticateAdmin(AdminLoginRequest loginRequest);
    
    Long getTotalUsers();
    
    Long getTotalVendors();
    
    Long getDailyTraffic();
}
