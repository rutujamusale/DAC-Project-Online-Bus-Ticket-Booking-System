
package com.bus_ticket.dto.Vendor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import com.bus_ticket.entities.Vendor.VendorStatus;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Vendor data transfer object")
public class VendorDto {
    
    @Schema(description = "Vendor ID", example = "1")
    private Long id;
    
    @NotBlank(message = "Vendor name is required")
    @Schema(description = "Vendor name", example = "Express Travels")
    private String vendorName;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    @Schema(description = "Vendor email", example = "vendor@example.com")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "Vendor password", example = "password123")
    private String password;
    
    @Schema(description = "Phone number", example = "+91-9876543210")
    private String phoneNumber;
    
    @Schema(description = "Address", example = "123 Main Street, Mumbai")
    private String address;
    
    @Schema(description = "License number", example = "LIC123456")
    private String licenseNumber;
    
    @Schema(description = "Vendor status", example = "APPROVED")
    private VendorStatus status;
}
