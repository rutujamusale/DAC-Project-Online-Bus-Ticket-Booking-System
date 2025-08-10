package com.bus_ticket.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.dao.UserDao;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dto.AdminLoginRequest;
import com.bus_ticket.dto.ApiResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private VendorDao vendorDao;
    
    @Override
    public ApiResponse authenticateAdmin(AdminLoginRequest loginRequest) {
        if ("admin".equals(loginRequest.getUsername()) && "1234".equals(loginRequest.getPassword())) {
            return new ApiResponse("Admin login successful");
        }
        throw new ApiException("Invalid admin credentials");
    }
    
    @Override
    public Long getTotalUsers() {
        return userDao.countActiveUsers();
    }
    
    @Override
    public Long getTotalVendors() {
        return vendorDao.countActiveVendors();
    }
    
    @Override
    public Long getDailyTraffic() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return userDao.countUsersByDateRange(startOfDay, endOfDay);
    }
}

