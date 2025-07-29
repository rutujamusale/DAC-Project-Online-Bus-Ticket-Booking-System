package com.bus_ticket.dto.Vendor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Vendor login request")
public class VendorLoginRequest {
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    @Schema(description = "Vendor email", example = "vendor@example.com")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "Vendor password", example = "password123")
    private String password;
    
    public VendorLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
