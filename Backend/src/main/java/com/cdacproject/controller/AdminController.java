package com.cdacproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cdacproject.dto.AdminLoginRequest;
import com.cdacproject.dto.ApiResponse;
import com.cdacproject.dto.VendorDto;
import com.cdacproject.services.AdminService;
import com.cdacproject.services.VendorService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private VendorService vendorService;
    
    @PostMapping("/login")

    public ResponseEntity<?> adminLogin(@Valid @RequestBody AdminLoginRequest loginRequest) {
        ApiResponse response = adminService.authenticateAdmin(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/dashboard")

    public ResponseEntity<?> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalUsers", adminService.getTotalUsers());
        dashboardData.put("totalVendors", adminService.getTotalVendors());
        dashboardData.put("dailyTraffic", adminService.getDailyTraffic());
        
        return ResponseEntity.ok(dashboardData);
    }
    
    @PostMapping("/vendors")

    public ResponseEntity<?> addVendor(@Valid @RequestBody VendorDto vendorDto) {
        ApiResponse response = vendorService.addVendor(vendorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/vendors")

    public ResponseEntity<?> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }
    
    @DeleteMapping("/vendors/{id}")

    public ResponseEntity<?> removeVendor(@PathVariable Long id) {
        ApiResponse response = vendorService.softDeleteVendor(id);
        return ResponseEntity.ok(response);
    }
}
