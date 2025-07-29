package com.bus_ticket.dto.Vendor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Vendor login response")
public class VendorLoginResponse {
    
    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Vendor ID", example = "1")
    private Long vendorId;
    
    @Schema(description = "Vendor name", example = "Express Travels")
    private String vendorName;
    
    @Schema(description = "Vendor email", example = "vendor@example.com")
    private String email;
    
    @Schema(description = "Response message", example = "Login successful")
    private String message;
}
