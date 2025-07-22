package com.cdacproject.services;

import com.cdacproject.dto.VendorDto;
import com.cdacproject.dto.ApiResponse;
import java.util.List;

public interface VendorService {
    
    ApiResponse addVendor(VendorDto vendorDto);
    
    List<VendorDto> getAllVendors();
    
    VendorDto getVendorById(Long id);
    
    ApiResponse updateVendor(Long id, VendorDto vendorDto);
    
    ApiResponse softDeleteVendor(Long id);
    
    ApiResponse authenticateVendor(String email, String password);

    VendorDto getVendorByEmail(String email);
}
