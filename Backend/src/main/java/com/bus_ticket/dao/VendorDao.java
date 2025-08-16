
package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Vendor;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorDao extends JpaRepository<Vendor, Long> {
    
    @Query("SELECT v FROM Vendor v WHERE v.email = :email")
    Optional<Vendor> findByEmail(String email);
    
    @Query("SELECT v FROM Vendor v WHERE v.email = :email AND v.isDeleted = false")
    Optional<Vendor> findActiveByEmail(String email);
    
    @Query("SELECT v FROM Vendor v WHERE v.email = :email AND v.password = :password")
    Optional<Vendor> findByEmailAndPassword(String email, String password);
    
    @Query("SELECT v FROM Vendor v WHERE v.isDeleted = false")
    List<Vendor> findAllActiveVendors();
    
    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.isDeleted = false")
    Long countActiveVendors();
    
    @Query("SELECT v FROM Vendor v WHERE v.licenseNumber = :licenseNumber")
    Optional<Vendor> findByLicenseNumber(String licenseNumber);
    
    @Query("SELECT v FROM Vendor v WHERE v.status = :status")
    List<Vendor> findByStatus(Vendor.VendorStatus status);
    
    @Query("SELECT v FROM Vendor v WHERE v.status = :status AND v.isDeleted = false")
    List<Vendor> findPendingVendors(@Param("status") Vendor.VendorStatus status);
    
    @Query("SELECT v FROM Vendor v WHERE v.status = 'PENDING' AND v.isDeleted = false")
    List<Vendor> findPendingVendorsDirect();
    
    @Query("SELECT v FROM Vendor v WHERE v.email = :email AND v.status = 'APPROVED' AND v.isDeleted = false")
    Optional<Vendor> findApprovedByEmail(String email);
}
