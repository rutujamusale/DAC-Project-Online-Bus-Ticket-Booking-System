
package com.bus_ticket.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dao.BusDao;
import com.bus_ticket.dao.ScheduleDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Vendor.VendorDto;
import com.bus_ticket.dto.Vendor.UpdateVendorDTO;
import com.bus_ticket.dto.Vendor.ChangePasswordDTO;
import com.bus_ticket.dto.Vendor.VendorRegistrationDTO;
import com.bus_ticket.entities.Vendor;
import com.bus_ticket.entities.Bus;
import com.bus_ticket.entities.Schedule;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.filter.JwtUtil;
import com.bus_ticket.dto.Vendor.VendorLoginResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {
    
    private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);
    
    @Autowired
    private VendorDao vendorDao;
    
    @Autowired
    private BusDao busDao;
    
    @Autowired
    private ScheduleDao scheduleDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public ApiResponse addVendor(VendorDto vendorDto) {
        logger.info("Adding vendor with email: {}", vendorDto.getEmail());
        
        // Check if vendor with email already exists
        Optional<Vendor> existingVendor = vendorDao.findByEmail(vendorDto.getEmail());
        
        if (existingVendor.isPresent()) {
            Vendor vendor = existingVendor.get();
            if (vendor.isDeleted() || vendor.getStatus() == Vendor.VendorStatus.REJECTED) {
                String reactivationType = vendor.isDeleted() ? "deleted" : "rejected";
                logger.info("Reactivating {} vendor with email: {}", reactivationType, vendorDto.getEmail());
                // Reactivate the vendor with updated details
                vendor.setVendorName(vendorDto.getVendorName());
                vendor.setPhoneNumber(vendorDto.getPhoneNumber());
                vendor.setAddress(vendorDto.getAddress());
                vendor.setLicenseNumber(vendorDto.getLicenseNumber());
                vendor.setPassword(passwordEncoder.encode(vendorDto.getPassword()));
                vendor.setDeleted(false);
                vendor.setStatus(Vendor.VendorStatus.APPROVED); // Admin is approving directly
                
                vendorDao.save(vendor);
                return new ApiResponse(true, "Vendor account reactivated and approved successfully");
            } else {
                throw new ApiException("Vendor with email " + vendorDto.getEmail() + " already exists");
            }
        }
        
        Vendor vendor = modelMapper.map(vendorDto, Vendor.class);
        // Encode password
        vendor.setPassword(passwordEncoder.encode(vendorDto.getPassword()));
        // Set status to APPROVED when admin adds vendor directly
        vendor.setStatus(Vendor.VendorStatus.APPROVED);
        vendor.setDeleted(false);
        
        vendorDao.save(vendor);
        logger.info("Vendor added successfully with ID: {}", vendor.getId());
        return new ApiResponse(true, "Vendor added successfully");
    }
    
    @Override
    public List<VendorDto> getAllVendors() {
        logger.info("Getting all active vendors");
        return vendorDao.findAllActiveVendors()
                .stream()
                .map(vendor -> modelMapper.map(vendor, VendorDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public VendorDto getVendorById(Long id) {
        logger.info("Getting vendor by ID: {}", id);
        
        Vendor vendor = vendorDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        
        if (vendor.isDeleted()) {
            throw new ResourceNotFoundException("Vendor not found with id: " + id);
        }
        
        return modelMapper.map(vendor, VendorDto.class);
    }
    
    @Override
    public ApiResponse updateVendor(Long id, VendorDto vendorDto) {
        logger.info("Updating vendor with ID: {}", id);
        
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
        logger.info("Vendor updated successfully: {}", id);
        return new ApiResponse(true, "Vendor updated successfully");
    }
    
    @Override
    public ApiResponse softDeleteVendor(Long id) {
        logger.info("Soft deleting vendor with ID: {}", id);
        
        try {
            Vendor vendor = vendorDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
            
            if (vendor.isDeleted()) {
                logger.warn("Vendor {} is already deleted", id);
                return new ApiResponse(false, "Vendor account is already deactivated");
            }
            
            // Soft delete the vendor
            vendor.setDeleted(true);
            vendorDao.save(vendor);
            
            // Hard delete all buses belonging to this vendor (since buses now use hard delete)
            List<Bus> vendorBuses = busDao.findByVendorId(id);
            logger.info("Found {} buses for vendor {}", vendorBuses.size(), id);
            
            int deletedBuses = 0;
            for (Bus bus : vendorBuses) {
                logger.info("Hard deleting bus: ID={}, Number={}", bus.getId(), bus.getBusNumber());
                
                // Delete all schedules for this bus first
                List<Schedule> busSchedules = scheduleDao.findAllByBusId(bus.getId());
                logger.info("Found {} schedules for bus {}", busSchedules.size(), bus.getId());
                
                for (Schedule schedule : busSchedules) {
                    scheduleDao.delete(schedule);
                }
                
                // Delete the bus
                busDao.delete(bus);
                deletedBuses++;
            }
            
            if (deletedBuses > 0) {
                busDao.flush();
                scheduleDao.flush();
            }
            
            logger.info("Vendor soft deleted successfully: {} (deleted {} buses)", id, deletedBuses);
            return new ApiResponse(true, "Vendor account deactivated and all associated buses/schedules removed successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("Vendor not found: {}", id, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error soft deleting vendor: {}", id, e);
            return new ApiResponse(false, "Error deactivating vendor: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse authenticateVendor(String email, String password) {
        logger.info("Authenticating vendor with email: {}", email);
        
        try {
            // Find vendor by email
            Optional<Vendor> vendorOpt = vendorDao.findByEmail(email);
            if (!vendorOpt.isPresent()) {
                logger.warn("Vendor not found with email: {}", email);
                return new ApiResponse(false, "Invalid credentials");
            }
            
            Vendor vendor = vendorOpt.get();
            
            // Check if vendor is deleted
            if (vendor.isDeleted()) {
                logger.warn("Vendor account is deactivated: {}", email);
                return new ApiResponse(false, "Account is deactivated");
            }
            
            // Check if vendor is approved
            if (vendor.getStatus() != Vendor.VendorStatus.APPROVED) {
                if (vendor.getStatus() == Vendor.VendorStatus.PENDING) {
                    logger.warn("Vendor account is pending approval: {}", email);
                    return new ApiResponse(false, "Account is pending admin approval");
                } else if (vendor.getStatus() == Vendor.VendorStatus.REJECTED) {
                    logger.warn("Vendor account is rejected: {}", email);
                    return new ApiResponse(false, "Account has been rejected by admin");
                }
            }
            
            // Check password with BCrypt
            if (!passwordEncoder.matches(password, vendor.getPassword())) {
                logger.warn("Invalid password for vendor: {}", email);
                return new ApiResponse(false, "Invalid credentials");
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(vendor.getEmail(), "VENDOR");
            
            // Create login response
            VendorLoginResponse response = new VendorLoginResponse();
            response.setToken(token);
            response.setVendorId(vendor.getId());
            response.setVendorName(vendor.getVendorName());
            response.setEmail(vendor.getEmail());
            response.setMessage("Login successful");
            
            logger.info("Vendor authenticated successfully: {}", email);
            return new ApiResponse(true, "Login successful", response);
            
        } catch (Exception e) {
            logger.error("Error authenticating vendor: {}", email, e);
            return new ApiResponse(false, "Error during authentication");
        }
    }

    @Override
    public VendorDto getVendorByEmail(String email) {
        logger.info("Getting vendor by email: {}", email);
        
        Vendor vendor = vendorDao.findActiveByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with email: " + email));
        
        return modelMapper.map(vendor, VendorDto.class);
    }
    
    @Override
    public ApiResponse deactivateVendor(Long id) {
        logger.info("Deactivating vendor with ID: {}", id);
        
        try {
            Vendor vendor = vendorDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
            
            if (vendor.isDeleted()) {
                logger.warn("Vendor {} is already deleted", id);
                return new ApiResponse(false, "Vendor account is already deactivated");
            }
            
            // Soft delete the vendor
            vendor.setDeleted(true);
            vendorDao.save(vendor);
            
            // Hard delete all buses belonging to this vendor (since buses now use hard delete)
            List<Bus> vendorBuses = busDao.findByVendorId(id);
            logger.info("Found {} buses for vendor {}", vendorBuses.size(), id);
            
            int deletedBuses = 0;
            for (Bus bus : vendorBuses) {
                logger.info("Hard deleting bus: ID={}, Number={}", bus.getId(), bus.getBusNumber());
                
                // Delete all schedules for this bus first
                List<Schedule> busSchedules = scheduleDao.findAllByBusId(bus.getId());
                logger.info("Found {} schedules for bus {}", busSchedules.size(), bus.getId());
                
                for (Schedule schedule : busSchedules) {
                    scheduleDao.delete(schedule);
                }
                
                // Delete the bus
                busDao.delete(bus);
                deletedBuses++;
            }
            
            if (deletedBuses > 0) {
                busDao.flush();
                scheduleDao.flush();
            }
            
            logger.info("Vendor deactivated successfully: {} (deleted {} buses)", id, deletedBuses);
            return new ApiResponse(true, "Vendor account deactivated and all associated buses/schedules removed successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("Vendor not found: {}", id, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error deactivating vendor: {}", id, e);
            return new ApiResponse(false, "Error deactivating vendor account: " + e.getMessage());
        }
    }
    

    
    @Override
    public ApiResponse updateVendorProfile(Long id, UpdateVendorDTO updateVendorDTO) {
        logger.info("Updating vendor profile with ID: {}", id);
        
        try {
            Vendor vendor = vendorDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
            
            if (vendor.isDeleted()) {
                logger.warn("Cannot update deleted vendor: {}", id);
                return new ApiResponse(false, "Cannot update deactivated vendor account");
            }
            
            // Check if email is being changed and if it already exists
            if (!vendor.getEmail().equals(updateVendorDTO.getEmail())) {
                Optional<Vendor> existingVendorWithEmail = vendorDao.findByEmail(updateVendorDTO.getEmail());
                if (existingVendorWithEmail.isPresent() && !existingVendorWithEmail.get().getId().equals(id)) {
                    logger.warn("Email {} already exists for another vendor", updateVendorDTO.getEmail());
                    return new ApiResponse(false, "Email address is already registered with another vendor");
                }
            }
            
            // Check if license number is being changed and if it already exists
            if (updateVendorDTO.getLicenseNumber() != null && 
                !updateVendorDTO.getLicenseNumber().equals(vendor.getLicenseNumber())) {
                Optional<Vendor> existingVendorWithLicense = vendorDao.findByLicenseNumber(updateVendorDTO.getLicenseNumber());
                if (existingVendorWithLicense.isPresent() && !existingVendorWithLicense.get().getId().equals(id)) {
                    logger.warn("License number {} already exists for another vendor", updateVendorDTO.getLicenseNumber());
                    return new ApiResponse(false, "License number is already registered with another vendor");
                }
            }
            
            // Update vendor details
            vendor.setVendorName(updateVendorDTO.getVendorName());
            vendor.setEmail(updateVendorDTO.getEmail());
            vendor.setPhoneNumber(updateVendorDTO.getPhoneNumber());
            vendor.setAddress(updateVendorDTO.getAddress());
            vendor.setLicenseNumber(updateVendorDTO.getLicenseNumber());
            
            vendorDao.save(vendor);
            
            logger.info("Vendor profile updated successfully: {}", id);
            return new ApiResponse(true, "Vendor profile updated successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("Vendor not found: {}", id, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating vendor profile: {}", id, e);
            return new ApiResponse(false, "Error updating vendor profile: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse changePassword(Long id, ChangePasswordDTO changePasswordDTO) {
        logger.info("Changing password for vendor with ID: {}", id);
        
        try {
            Vendor vendor = vendorDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
            
            if (vendor.isDeleted()) {
                logger.warn("Cannot change password for deleted vendor: {}", id);
                return new ApiResponse(false, "Cannot change password for deactivated vendor account");
            }
            
            // Verify current password
            if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), vendor.getPassword())) {
                logger.warn("Invalid current password for vendor: {}", id);
                return new ApiResponse(false, "Current password is incorrect");
            }
            
            // Check if new password matches confirm password
            if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                logger.warn("New password and confirm password do not match for vendor: {}", id);
                return new ApiResponse(false, "New password and confirm password do not match");
            }
            
            // Check if new password is same as current password
            if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), vendor.getPassword())) {
                logger.warn("New password is same as current password for vendor: {}", id);
                return new ApiResponse(false, "New password must be different from current password");
            }
            
            // Encode and set new password
            vendor.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            vendorDao.save(vendor);
            
            logger.info("Password changed successfully for vendor: {}", id);
            return new ApiResponse(true, "Password changed successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("Vendor not found: {}", id, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error changing password for vendor: {}", id, e);
            return new ApiResponse(false, "Error changing password: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse registerVendor(VendorRegistrationDTO registrationDTO) {
        logger.info("Registering new vendor with email: {}", registrationDTO.getEmail());
        
        try {
            // Check if email already exists
            Optional<Vendor> existingVendorWithEmail = vendorDao.findByEmail(registrationDTO.getEmail());
            if (existingVendorWithEmail.isPresent()) {
                Vendor existingVendor = existingVendorWithEmail.get();
                
                // If vendor is soft-deleted or rejected, allow reactivation
                if (existingVendor.isDeleted() || existingVendor.getStatus() == Vendor.VendorStatus.REJECTED) {
                    String reactivationType = existingVendor.isDeleted() ? "deleted" : "rejected";
                    logger.info("Reactivating {} vendor with email: {}", reactivationType, registrationDTO.getEmail());
                    
                    // Check if license number is different from the existing one
                    if (!existingVendor.getLicenseNumber().equals(registrationDTO.getLicenseNumber())) {
                        // Check if the new license number already exists for another vendor
                        Optional<Vendor> existingVendorWithLicense = vendorDao.findByLicenseNumber(registrationDTO.getLicenseNumber());
                        if (existingVendorWithLicense.isPresent() && !existingVendorWithLicense.get().getId().equals(existingVendor.getId())) {
                            logger.warn("License number {} already exists for another vendor", registrationDTO.getLicenseNumber());
                            return new ApiResponse(false, "License number is already registered with another vendor");
                        }
                    }
                    
                    // Reactivate the vendor with updated details
                    existingVendor.setVendorName(registrationDTO.getVendorName());
                    existingVendor.setPhoneNumber(registrationDTO.getPhoneNumber());
                    existingVendor.setAddress(registrationDTO.getAddress());
                    existingVendor.setLicenseNumber(registrationDTO.getLicenseNumber());
                    existingVendor.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
                    existingVendor.setDeleted(false);
                    existingVendor.setStatus(Vendor.VendorStatus.PENDING); // Reset to pending for admin approval
                    
                    vendorDao.save(existingVendor);
                    
                    logger.info("Vendor account reactivated successfully: {}", existingVendor.getId());
                    return new ApiResponse(true, "Account reactivated successfully! Please wait for admin approval.");
                } else {
                    logger.warn("Email {} already exists for active vendor", registrationDTO.getEmail());
                    return new ApiResponse(false, "Email address is already registered");
                }
            }
            
            // Check if license number already exists for new registration
            Optional<Vendor> existingVendorWithLicense = vendorDao.findByLicenseNumber(registrationDTO.getLicenseNumber());
            if (existingVendorWithLicense.isPresent()) {
                logger.warn("License number {} already exists", registrationDTO.getLicenseNumber());
                return new ApiResponse(false, "License number is already registered");
            }
            
            // Create new vendor with PENDING status
            Vendor vendor = new Vendor();
            vendor.setVendorName(registrationDTO.getVendorName());
            vendor.setEmail(registrationDTO.getEmail());
            vendor.setPhoneNumber(registrationDTO.getPhoneNumber());
            vendor.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            vendor.setAddress(registrationDTO.getAddress());
            vendor.setLicenseNumber(registrationDTO.getLicenseNumber());
            vendor.setStatus(Vendor.VendorStatus.PENDING);
            vendor.setDeleted(false);
            
            vendorDao.save(vendor);
            
            logger.info("Vendor registration submitted successfully: {}", vendor.getId());
            return new ApiResponse(true, "Registration submitted successfully! Please wait for admin approval.");
            
        } catch (Exception e) {
            logger.error("Error registering vendor: {}", registrationDTO.getEmail(), e);
            return new ApiResponse(false, "Error registering vendor: " + e.getMessage());
        }
    }
    
    @Override
    public List<VendorDto> getPendingVendors() {
        logger.info("Getting pending vendors");
        
        try {
            // Try both methods to see which one works
            List<Vendor> pendingVendors = vendorDao.findPendingVendors(Vendor.VendorStatus.PENDING);
            logger.info("Method 1 - Found {} pending vendors using enum parameter", pendingVendors.size());
            
            if (pendingVendors.isEmpty()) {
                // Try the direct method
                List<Vendor> directPendingVendors = vendorDao.findPendingVendorsDirect();
                logger.info("Method 2 - Found {} pending vendors using direct query", directPendingVendors.size());
                pendingVendors = directPendingVendors;
            }
            
            // Debug: Check if vendors have data
            for (Vendor vendor : pendingVendors) {
                logger.info("Raw vendor data: id={}, email={}, vendorName={}, phoneNumber={}, address={}, licenseNumber={}, status={}", 
                    vendor.getId(), vendor.getEmail(), vendor.getVendorName(), 
                    vendor.getPhoneNumber(), vendor.getAddress(), vendor.getLicenseNumber(), vendor.getStatus());
            }
            
            List<VendorDto> result = pendingVendors.stream()
                    .map(vendor -> {
                        // Use manual mapping to ensure all fields are properly set
                        VendorDto dto = new VendorDto();
                        dto.setId(vendor.getId());
                        dto.setVendorName(vendor.getVendorName() != null ? vendor.getVendorName() : "Unknown");
                        dto.setEmail(vendor.getEmail() != null ? vendor.getEmail() : "No Email");
                        dto.setPhoneNumber(vendor.getPhoneNumber() != null ? vendor.getPhoneNumber() : "No Phone");
                        dto.setAddress(vendor.getAddress() != null ? vendor.getAddress() : "No Address");
                        dto.setLicenseNumber(vendor.getLicenseNumber() != null ? vendor.getLicenseNumber() : "No License");
                        dto.setStatus(vendor.getStatus());
                        // Don't set password for security
                        
                        logger.info("Final DTO: id={}, email={}, vendorName={}, phoneNumber={}, address={}, licenseNumber={}", 
                            dto.getId(), dto.getEmail(), dto.getVendorName(), 
                            dto.getPhoneNumber(), dto.getAddress(), dto.getLicenseNumber());
                        
                        return dto;
                    })
                    .collect(Collectors.toList());
            
            logger.info("Returning {} pending vendor DTOs", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error getting pending vendors", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public ApiResponse approveVendor(Long vendorId) {
        logger.info("Approving vendor with ID: {}", vendorId);
        
        try {
            Vendor vendor = vendorDao.findById(vendorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
            
            logger.info("Found vendor: id={}, email={}, status={}", vendor.getId(), vendor.getEmail(), vendor.getStatus());
            
            if (vendor.getStatus() != Vendor.VendorStatus.PENDING) {
                logger.warn("Vendor {} is not in pending status, current status: {}", vendorId, vendor.getStatus());
                return new ApiResponse(false, "Vendor is not in pending status");
            }
            
            vendor.setStatus(Vendor.VendorStatus.APPROVED);
            vendorDao.save(vendor);
            
            logger.info("Vendor approved successfully: {}", vendorId);
            return new ApiResponse(true, "Vendor approved successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("Vendor not found: {}", vendorId, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error approving vendor: {}", vendorId, e);
            return new ApiResponse(false, "Error approving vendor: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse rejectVendor(Long vendorId) {
        logger.info("Rejecting vendor with ID: {}", vendorId);
        
        try {
            Vendor vendor = vendorDao.findById(vendorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
            
            if (vendor.getStatus() != Vendor.VendorStatus.PENDING) {
                logger.warn("Vendor {} is not in pending status", vendorId);
                return new ApiResponse(false, "Vendor is not in pending status");
            }
            
            vendor.setStatus(Vendor.VendorStatus.REJECTED);
            vendorDao.save(vendor);
            
            logger.info("Vendor rejected successfully: {}", vendorId);
            return new ApiResponse(true, "Vendor rejected successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("Vendor not found: {}", vendorId, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("Error rejecting vendor: {}", vendorId, e);
            return new ApiResponse(false, "Error rejecting vendor: " + e.getMessage());
        }
    }
}
