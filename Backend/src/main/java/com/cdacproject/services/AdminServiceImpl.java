package com.cdacproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cdacproject.dao.AdminDao;
import com.cdacproject.dao.UserDao;
import com.cdacproject.dao.VendorDao;
import com.cdacproject.dto.AdminLoginRequest;
import com.cdacproject.dto.ApiResponse;
import com.cdacproject.custom_exceptions.ApiException;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminDao adminDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private VendorDao vendorDao;
    
    @Override
    public ApiResponse authenticateAdmin(AdminLoginRequest loginRequest) {
        // Hardcoded admin credentials
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
