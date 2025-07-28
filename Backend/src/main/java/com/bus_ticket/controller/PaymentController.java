package com.bus_ticket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bus_ticket.dto.Payment.PaymentRequestDTO;
import com.bus_ticket.dto.Payment.PaymentResponseDTO;
import com.bus_ticket.services.PaymentService;

import lombok.AllArgsConstructor;
@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Create a new payment
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDTO dto) {
        PaymentResponseDTO createdPayment = paymentService.createPayment(dto);
        return ResponseEntity.ok(createdPayment);
    }

    // Get a payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    // Get all payments (excluding soft-deleted)
    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Update an existing payment
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentRequestDTO dto) {
        PaymentResponseDTO updatedPayment = paymentService.updatePayment(id, dto);
        return ResponseEntity.ok(updatedPayment);
    }

    // Soft delete a payment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted (soft delete applied).");
    }
}
