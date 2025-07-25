package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.VendorLoginRequest;
import com.bus_ticket.dto.VendorLoginResponse;
import com.bus_ticket.services.VendorService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Vendor Management", description = "APIs for vendor operations")
public class VendorApiController {
    
    @Autowired
    private VendorService vendorService;

    @PostMapping("/login")
    @Operation(summary = "Vendor login", description = "Authenticate vendor with email and password")

    public ResponseEntity<?> login(@Valid @RequestBody VendorLoginRequest loginRequest) {
        try {
            // Authenticate vendor
            ApiResponse authResponse = vendorService.authenticateVendor(loginRequest.getEmail(), loginRequest.getPassword());
            
            // Get vendor details
            var vendor = vendorService.getVendorByEmail(loginRequest.getEmail());
            
            VendorLoginResponse response = new VendorLoginResponse();
            response.setVendorId(vendor.getId());
            response.setVendorName(vendor.getVendorName());
            response.setEmail(vendor.getEmail());
            response.setMessage("Login successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
}
