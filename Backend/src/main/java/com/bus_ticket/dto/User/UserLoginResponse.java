package com.bus_ticket.dto.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "User login response")
public class UserLoginResponse {
    
    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @Schema(description = "User email", example = "user@example.com")
    private String email;
    
    @Schema(description = "User first name", example = "John")
    private String firstName;
    
    @Schema(description = "User last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "Response message", example = "Login successful")
    private String message;
}
