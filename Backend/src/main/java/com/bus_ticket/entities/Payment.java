package com.bus_ticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseEntity {
    
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;
    
    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Column(name = "total_price")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "transaction_reference", unique = true)
    private String transactionReference;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(name = "gateway_response_code")
    private String gatewayResponseCode;

    @Column(name = "gateway_response_message")
    private String gatewayResponseMessage;

    @Column(name = "failure_reason")
    private String failureReason;

    // Payment method specific details
    @Column(name = "card_last_four_digits")
    private String cardLastFourDigits;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "upi_id")
    private String upiId;

    @Column(name = "wallet_name")
    private String walletName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // Constructor
    public Payment(Transaction transaction, Double totalPrice, PaymentMethod paymentMethod) {
        this.transaction = transaction;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
    }

    // Methods
    public void markAsCompleted(String gatewayTransactionId, String responseCode, String responseMessage) {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.gatewayTransactionId = gatewayTransactionId;
        this.gatewayResponseCode = responseCode;
        this.gatewayResponseMessage = responseMessage;
        this.paymentDate = LocalDateTime.now();
    }

    public void markAsFailed(String failureReason, String responseCode, String responseMessage) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.failureReason = failureReason;
        this.gatewayResponseCode = responseCode;
        this.gatewayResponseMessage = responseMessage;
        this.paymentDate = LocalDateTime.now();
    }

    // Enums
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, WALLET, CASH, CHEQUE
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, CANCELLED, REFUNDED
    }
}
