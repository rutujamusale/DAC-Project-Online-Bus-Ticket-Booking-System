package com.cdacproject.services;

import com.cdacproject.dto.AdminLoginRequest;
import com.cdacproject.dto.ApiResponse;

public interface AdminService {
    
    ApiResponse authenticateAdmin(AdminLoginRequest loginRequest);
    
    Long getTotalUsers();
    
    Long getTotalVendors();
    
    Long getDailyTraffic();
}
