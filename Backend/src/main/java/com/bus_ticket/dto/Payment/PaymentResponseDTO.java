package com.bus_ticket.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long transactionId;
    private Long bookingId;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionReference;
    private String gatewayTransactionId;
    private Double amount;
    private Double processingFee;
    private Double taxAmount;
    private Double discountAmount;
    private Double finalAmount;
    private String currency;
    private LocalDateTime paymentDate;
    private String message;
    private String gatewayResponseCode;
    private String gatewayResponseMessage;
    private String failureReason;
    private boolean success;
    
    // Payment method specific details
    private String cardLastFourDigits;
    private String cardType;
    private String bankName;
    private String upiId;
    private String walletName;
    
    // Additional details
    private Integer retryCount;
    private String deviceInfo;
    private String ipAddress;
    
    // Constructor for backward compatibility
    public PaymentResponseDTO(Long transactionId, Long bookingId, String paymentStatus, 
                            String paymentMethod, String transactionReference, Double amount, 
                            LocalDateTime paymentDate, String message, boolean success) {
        this.transactionId = transactionId;
        this.bookingId = bookingId;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.message = message;
        this.success = success;
        this.currency = "INR";
        this.finalAmount = amount;
    }
}
