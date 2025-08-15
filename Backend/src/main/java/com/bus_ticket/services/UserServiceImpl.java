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
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
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
        logger.info("Registering user with email: {}", userDto.getEmail());
        
        Optional<User> existingUser = userDao.findByEmail(userDto.getEmail());
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.isDeleted()) {
                logger.info("Reactivating deleted user with email: {}", userDto.getEmail());
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setPhone(userDto.getPhone());
                user.setAddress(userDto.getAddress());
                user.setCity(userDto.getCity());
                user.setState(userDto.getState());
                user.setPincode(userDto.getPincode());
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                user.setDeleted(false);
                
                userDao.save(user);
                return new ApiResponse(true, "User account reactivated successfully");
            } else {
                throw new ApiException("User with email " + userDto.getEmail() + " already exists");
            }
        }
        
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDao.save(user);
        logger.info("User registered successfully with ID: {}", user.getId());
        return new ApiResponse(true, "User registered successfully");
    }
    
    @Override
    public ApiResponse authenticateUser(UserLoginRequest loginRequest) {
        logger.info("Authenticating user with email: {}", loginRequest.getEmail());
        
        User user = userDao.findActiveByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ApiException("Invalid credentials"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), "USER");
        
        logger.info("User authenticated successfully: {}", user.getEmail());
        return new ApiResponse(true, "User login successful. Token: " + token);
    }
    
    @Override
    public ApiResponse updateUser(Long id, UserDto userDto) {
        logger.info("Updating user with ID: {}", id);
        
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setCity(userDto.getCity());
        user.setState(userDto.getState());
        user.setPincode(userDto.getPincode());
        
        userDao.save(user);
        logger.info("User updated successfully: {}", id);
        return new ApiResponse(true, "User updated successfully");
    }
    
    @Override
    public ApiResponse deactivateUser(Long id) {
        logger.info("Deactivating user with ID: {}", id);
        
        try {
            User user = userDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
            
            if (user.isDeleted()) {
                logger.warn("User {} is already deleted", id);
                return new ApiResponse(false, "User account is already deactivated");
            }
            
            user.setDeleted(true);
            userDao.save(user);
            logger.info("User deactivated successfully: {}", id);
            
            return new ApiResponse(true, "User account deactivated successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", id, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error deactivating user: {}", id, e);
            return new ApiResponse(false, "Error deactivating user account: " + e.getMessage());
        }
    }
}
