package com.bus_ticket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Admin login request")
public class AdminLoginRequest {
    
    @NotBlank(message = "Username is required")
    @Schema(description = "Admin username", example = "admin")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "Admin password", example = "1234")
    private String password;
    
    public AdminLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
