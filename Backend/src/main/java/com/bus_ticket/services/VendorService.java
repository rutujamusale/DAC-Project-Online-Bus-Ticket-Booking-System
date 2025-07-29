package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Vendor.VendorDto;

public interface VendorService {
    
    ApiResponse addVendor(VendorDto vendorDto);
    
    List<VendorDto> getAllVendors();
    
    VendorDto getVendorById(Long id);
    
    ApiResponse updateVendor(Long id, VendorDto vendorDto);
    
    ApiResponse softDeleteVendor(Long id);
    
    ApiResponse authenticateVendor(String email, String password);

    VendorDto getVendorByEmail(String email);
}
