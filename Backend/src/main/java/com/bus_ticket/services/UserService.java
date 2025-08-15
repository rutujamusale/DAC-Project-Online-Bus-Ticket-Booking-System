package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.User.UserDto;
import com.bus_ticket.dto.User.UserLoginRequest;

public interface UserService {
    
    ApiResponse registerUser(UserDto userDto);
    
    ApiResponse authenticateUser(UserLoginRequest loginRequest);
    
    ApiResponse updateUser(Long id, UserDto userDto);
    
    ApiResponse deactivateUser(Long id);
}
