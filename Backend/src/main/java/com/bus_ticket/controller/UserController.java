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
            ApiResponse authResponse = userService.authenticateUser(loginRequest);
            
            var user = userDao.findByEmail(loginRequest.getEmail()).get();
            
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
    
    @GetMapping("/id/{userId}")
    @Operation(summary = "Get user by ID", description = "Get user profile by user ID")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            userDto.setAddress(user.getAddress());
            userDto.setCity(user.getCity());
            userDto.setState(user.getState());
            userDto.setPincode(user.getPincode());
            
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile", description = "Update user profile information")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserDto userDto) {
        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());
            user.setCity(userDto.getCity());
            user.setState(userDto.getState());
            user.setPincode(userDto.getPincode());
            
            User updatedUser = userDao.save(user);
            
            UserDto responseDto = new UserDto();
            responseDto.setId(updatedUser.getId());
            responseDto.setFirstName(updatedUser.getFirstName());
            responseDto.setLastName(updatedUser.getLastName());
            responseDto.setEmail(updatedUser.getEmail());
            responseDto.setPhone(updatedUser.getPhone());
            responseDto.setAddress(updatedUser.getAddress());
            responseDto.setCity(updatedUser.getCity());
            responseDto.setState(updatedUser.getState());
            responseDto.setPincode(updatedUser.getPincode());
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @PutMapping("/{userId}/deactivate")
    @Operation(summary = "Deactivate user account", description = "Soft delete user account")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Invalid user ID"));
            }
            
            ApiResponse response = userService.deactivateUser(userId);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error deactivating user: " + e.getMessage()));
        }
    }
}
