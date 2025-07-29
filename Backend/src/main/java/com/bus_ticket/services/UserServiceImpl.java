package com.bus_ticket.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus_ticket.dao.UserDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.User.UserDto;
import com.bus_ticket.dto.User.UserLoginRequest;
import com.bus_ticket.entities.User;
import com.bus_ticket.filter.JwtUtil;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public ApiResponse registerUser(UserDto userDto) {
        // Check if user with email already exists
        if (userDao.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ApiException("User with email " + userDto.getEmail() + " already exists");
        }
        
        User user = modelMapper.map(userDto, User.class);
        // Encode password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDao.save(user);
        return new ApiResponse("User registered successfully");
    }
    
    @Override
    public ApiResponse authenticateUser(UserLoginRequest loginRequest) {
        User user = userDao.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ApiException("Invalid credentials"));
        
        if (user.isDeleted()) {
            throw new ApiException("Account has been deactivated");
        }
        
        // Check password with BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid credentials");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), "USER");
        
        return new ApiResponse("User login successful. Token: " + token);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User account has been deactivated");
        }
        
        return modelMapper.map(user, UserDto.class);
    }
    
    @Override
    public ApiResponse updateUser(Long id, UserDto userDto) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        
        // Update user details
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        
        userDao.save(user);
        return new ApiResponse("User updated successfully");
    }
    
    @Override
    public ApiResponse softDeleteUser(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setDeleted(true);
        userDao.save(user);
        return new ApiResponse("User account deactivated successfully");
    }
}
