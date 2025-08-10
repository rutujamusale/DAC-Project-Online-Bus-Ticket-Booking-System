
package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Vendor.VendorDto;
import com.bus_ticket.dto.Vendor.UpdateVendorDTO;
import com.bus_ticket.dto.Vendor.ChangePasswordDTO;
import com.bus_ticket.dto.Vendor.VendorRegistrationDTO;

public interface VendorService {
    
    ApiResponse addVendor(VendorDto vendorDto);
    
    List<VendorDto> getAllVendors();
    
    VendorDto getVendorById(Long id);
    
    ApiResponse updateVendor(Long id, VendorDto vendorDto);
    
    ApiResponse softDeleteVendor(Long id);
    
    ApiResponse authenticateVendor(String email, String password);

    VendorDto getVendorByEmail(String email);
    
    ApiResponse deactivateVendor(Long id);
    
    ApiResponse updateVendorProfile(Long id, UpdateVendorDTO updateVendorDTO);
    
    ApiResponse changePassword(Long id, ChangePasswordDTO changePasswordDTO);
    
    ApiResponse registerVendor(VendorRegistrationDTO registrationDTO);
    
    List<VendorDto> getPendingVendors();
    
    ApiResponse approveVendor(Long vendorId);
    
    ApiResponse rejectVendor(Long vendorId);
}

