
package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "vendors")
@Getter
@Setter

public class Vendor extends BaseEntity {
    
    @Column(name = "vendor_name", nullable = false)
    private String vendorName;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "license_number", unique = true)
    private String licenseNumber;
    
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VendorStatus status = VendorStatus.PENDING;
    
    // Constructor to ensure status is set
    public Vendor() {
        this.status = VendorStatus.PENDING;
    }
    
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bus> buses;
    
    public enum VendorStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}

