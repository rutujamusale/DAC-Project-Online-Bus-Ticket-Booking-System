package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bus_ticket.dto.AdminLoginRequest;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Vendor.VendorDto;
import com.bus_ticket.services.AdminService;
import com.bus_ticket.services.VendorService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
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
    
    @GetMapping("/vendors/pending")
    public ResponseEntity<?> getPendingVendors() {
        try {
            List<VendorDto> pendingVendors = vendorService.getPendingVendors();
            return ResponseEntity.ok(pendingVendors);
        } catch (Exception e) {
            logger.error("Error getting pending vendors", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error getting pending vendors: " + e.getMessage()));
        }
    }
    
    @PostMapping("/vendors/{vendorId}/approve")
    public ResponseEntity<?> approveVendor(@PathVariable Long vendorId) {
        ApiResponse response = vendorService.approveVendor(vendorId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/vendors/{vendorId}/reject")
    public ResponseEntity<?> rejectVendor(@PathVariable Long vendorId) {
        ApiResponse response = vendorService.rejectVendor(vendorId);
        return ResponseEntity.ok(response);
    }
}
