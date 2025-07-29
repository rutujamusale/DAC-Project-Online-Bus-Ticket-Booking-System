package com.bus_ticket.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Vendor.VendorDto;
import com.bus_ticket.entities.Vendor;
import com.bus_ticket.filter.JwtUtil;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {
    
    @Autowired
    private VendorDao vendorDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public ApiResponse addVendor(VendorDto vendorDto) {
        // Check if vendor with email already exists
        if (vendorDao.findByEmail(vendorDto.getEmail()).isPresent()) {
            throw new ApiException("Vendor with email " + vendorDto.getEmail() + " already exists");
        }
        
        Vendor vendor = modelMapper.map(vendorDto, Vendor.class);
        // Encode password
        vendor.setPassword(passwordEncoder.encode(vendorDto.getPassword()));
        
        vendorDao.save(vendor);
        return new ApiResponse("Vendor added successfully");
    }
    
    @Override
    public List<VendorDto> getAllVendors() {
        return vendorDao.findAllActiveVendors()
                .stream()
                .map(vendor -> modelMapper.map(vendor, VendorDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public VendorDto getVendorById(Long id) {
        Vendor vendor = vendorDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        
        if (vendor.isDeleted()) {
            throw new ResourceNotFoundException("Vendor not found with id: " + id);
        }
        
        return modelMapper.map(vendor, VendorDto.class);
    }
    
    @Override
    public ApiResponse updateVendor(Long id, VendorDto vendorDto) {
        Vendor vendor = vendorDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        
        if (vendor.isDeleted()) {
            throw new ResourceNotFoundException("Vendor not found with id: " + id);
        }
        
        // Update vendor details
        vendor.setVendorName(vendorDto.getVendorName());
        vendor.setPhoneNumber(vendorDto.getPhoneNumber());
        vendor.setAddress(vendorDto.getAddress());
        vendor.setLicenseNumber(vendorDto.getLicenseNumber());
        
        vendorDao.save(vendor);
        return new ApiResponse("Vendor updated successfully");
    }
    
    @Override
    public ApiResponse softDeleteVendor(Long id) {
        Vendor vendor = vendorDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        
        vendor.setDeleted(true);
        vendorDao.save(vendor);
        return new ApiResponse("Vendor removed successfully");
    }
    
    @Override
    public ApiResponse authenticateVendor(String email, String password) {
        Vendor vendor = vendorDao.findByEmail(email)
                .orElseThrow(() -> new ApiException("Invalid credentials"));
        
        if (vendor.isDeleted()) {
            throw new ApiException("Account has been deactivated");
        }
        
        // Check password with BCrypt
        if (!passwordEncoder.matches(password, vendor.getPassword())) {
            throw new ApiException("Invalid credentials");
        }
        
        return new ApiResponse("Vendor login successful");
    }

    @Override
    public VendorDto getVendorByEmail(String email) {
        Vendor vendor = vendorDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with email: " + email));
        
        if (vendor.isDeleted()) {
            throw new ResourceNotFoundException("Vendor account has been deactivated");
        }
        
        return modelMapper.map(vendor, VendorDto.class);
    }
}
