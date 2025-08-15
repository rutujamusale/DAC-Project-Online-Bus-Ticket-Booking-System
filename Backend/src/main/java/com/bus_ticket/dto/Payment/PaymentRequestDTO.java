package com.bus_ticket.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long bookingId;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, WALLET, CASH, CHEQUE
    
    // Card payment details
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private String cardType; // VISA, MASTERCARD, AMEX, etc.
    private String cardLastFourDigits;
    
    // UPI payment details
    private String upiId;
    
    // Net banking details
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    
    // Wallet payment details
    private String walletType;
    private String walletId;
    private String walletName; // PAYTM, PHONEPE, etc.
    
    // Additional payment details
    private Double processingFee;
    private Double taxAmount;
    private Double discountAmount;
    private String currency;
    
    // Security and validation
    private String otp;
    private String transactionPassword;
    
    // Optional fields for enhanced tracking
    private String deviceInfo;
    private String ipAddress;
    private String userAgent;
}
