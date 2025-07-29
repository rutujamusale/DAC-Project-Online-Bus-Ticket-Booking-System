package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bus_ticket.dao.UserDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.User.UserDto;
import com.bus_ticket.dto.User.UserLoginRequest;
import com.bus_ticket.dto.User.UserLoginResponse;
import com.bus_ticket.entities.User;
import com.bus_ticket.filter.JwtUtil;
import com.bus_ticket.services.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User Management", description = "APIs for user operations")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        ApiResponse response = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        try {
            // Authenticate user
            ApiResponse authResponse = userService.authenticateUser(loginRequest);
            
            // Get user details
            var user = userDao.findByEmail(loginRequest.getEmail()).get();
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), "USER");
            
            UserLoginResponse response = new UserLoginResponse();
            response.setToken(token);
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setMessage("Login successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
    @GetMapping("/{email}")
    @Operation(summary = "Get user profile", description = "Get user profile by email")
    public ResponseEntity<?> getUserProfile(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
