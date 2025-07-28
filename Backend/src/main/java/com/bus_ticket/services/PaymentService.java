package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.Payment.PaymentRequestDTO;
import com.bus_ticket.dto.Payment.PaymentResponseDTO;

public interface PaymentService {
   // Create a new payment
    PaymentResponseDTO createPayment(PaymentRequestDTO dto);

    // Get a single payment by ID (only if not soft-deleted)
    PaymentResponseDTO getPaymentById(Long id);

    // Get all payments that are not soft-deleted
    List<PaymentResponseDTO> getAllPayments();

    // Update an existing payment
    PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto);

    // Soft delete a payment
    void deletePayment(Long id);
}