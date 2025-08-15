package com.bus_ticket.dto.Vendor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Vendor update data transfer object")
public class UpdateVendorDTO {
    
    @Schema(description = "Vendor ID", example = "1")
    private Long id;
    
    @NotBlank(message = "Vendor name is required")
    @Schema(description = "Vendor name", example = "Express Travels")
    private String vendorName;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    @Schema(description = "Vendor email", example = "vendor@example.com")
    private String email;
    
    @Pattern(regexp = "^[6-9]\\d{9}$|^\\+91[6-9]\\d{9}$|^91[6-9]\\d{9}$", message = "Invalid Indian contact number")
    @Schema(description = "Phone number", example = "+91-9876543210")
    private String phoneNumber;
    
    @Schema(description = "Address", example = "123 Main Street, Mumbai")
    private String address;
    
    @Schema(description = "License number", example = "LIC123456")
    private String licenseNumber;
}
