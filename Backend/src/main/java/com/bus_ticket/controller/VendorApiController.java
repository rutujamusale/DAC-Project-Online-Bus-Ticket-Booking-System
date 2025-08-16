package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Vendor.VendorLoginRequest;
import com.bus_ticket.dto.Vendor.UpdateVendorDTO;
import com.bus_ticket.dto.Vendor.ChangePasswordDTO;
import com.bus_ticket.filter.JwtUtil;
import com.bus_ticket.services.VendorService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bus_ticket.dto.Vendor.VendorRegistrationDTO;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Vendor Management", description = "APIs for vendor operations")
public class VendorApiController {
    
    @Autowired
    private VendorService vendorService;
    
    @Autowired
    private VendorDao vendorDao;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Vendor login", description = "Authenticate vendor with email and password")
    public ResponseEntity<?> login(@Valid @RequestBody VendorLoginRequest loginRequest) {
        try {
            ApiResponse response = vendorService.authenticateVendor(loginRequest.getEmail(), loginRequest.getPassword());
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response.getData());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error during login: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    @Operation(summary = "Vendor registration", description = "Register a new vendor (requires admin approval)")
    public ResponseEntity<?> registerVendor(@Valid @RequestBody VendorRegistrationDTO registrationDTO) {
        try {
            ApiResponse response = vendorService.registerVendor(registrationDTO);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error during registration: " + e.getMessage()));
        }
    }

    @GetMapping("/profile/{email}")
    @Operation(summary = "Get vendor profile", description = "Get vendor profile information by email")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        try {
            var vendor = vendorService.getVendorByEmail(email);
            return ResponseEntity.ok(vendor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }
    
    @PutMapping("/{vendorId}/deactivate")
    @Operation(summary = "Deactivate vendor account", description = "Soft delete vendor account")
    public ResponseEntity<?> deactivateVendor(@PathVariable Long vendorId) {
        try {
            if (vendorId == null || vendorId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Invalid vendor ID"));
            }
            
            ApiResponse response = vendorService.deactivateVendor(vendorId);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error deactivating vendor: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{vendorId}/profile")
    @Operation(summary = "Update vendor profile", description = "Update vendor profile information")
    public ResponseEntity<?> updateVendorProfile(@PathVariable Long vendorId, @Valid @RequestBody UpdateVendorDTO updateVendorDTO) {
        try {
            if (vendorId == null || vendorId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Invalid vendor ID"));
            }
            
            ApiResponse response = vendorService.updateVendorProfile(vendorId, updateVendorDTO);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating vendor profile: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{vendorId}/change-password")
    @Operation(summary = "Change vendor password", description = "Change vendor password with current password verification")
    public ResponseEntity<?> changePassword(@PathVariable Long vendorId, @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            if (vendorId == null || vendorId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Invalid vendor ID"));
            }
            
            ApiResponse response = vendorService.changePassword(vendorId, changePasswordDTO);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error changing password: " + e.getMessage()));
        }
    }
}
