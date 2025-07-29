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
@Schema(description = "User login request")
public class UserLoginRequest {
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    @Schema(description = "User email", example = "user@example.com")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    private String password;
}
