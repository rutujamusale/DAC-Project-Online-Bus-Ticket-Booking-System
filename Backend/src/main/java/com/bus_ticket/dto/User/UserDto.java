package com.bus_ticket.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "User data transfer object")
public class UserDto {
    
    @Schema(description = "User ID", example = "1")
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Schema(description = "First name", example = "John")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "password123")
    private String password;
    
    @Schema(description = "Phone number", example = "+91-9876543210")
    private String phone;
    
    @Schema(description = "Address", example = "123 Main Street, Mumbai")
    private String address;
    
    @Schema(description = "City", example = "Mumbai")
    private String city;
    
    @Schema(description = "State", example = "Maharashtra")
    private String state;
    
    @Schema(description = "Pincode", example = "400001")
    private String pincode;
}
