package com.bus_ticket.services;

import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.dao.PaymentDao;
import com.bus_ticket.dao.TransactionDao;
import com.bus_ticket.dto.Payment.PaymentRequestDTO;
import com.bus_ticket.dto.Payment.PaymentResponseDTO;
import com.bus_ticket.entities.Payment;
import com.bus_ticket.entities.Transaction;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private final PaymentDao paymentDao;

    @Autowired
    private final TransactionDao transactionDao;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setTotalPrice(dto.getTotalPrice());
        payment.setTransaction(getTransactionById(dto.getTransactionId()));
        payment.setDeleted(false);

        Payment saved = paymentDao.save(payment);
        return convertToResponseDTO(saved);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = findActivePayment(id);
        return convertToResponseDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentDao.findAll().stream()
                .filter(p -> !p.isDeleted())
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto) {
        Payment payment = findActivePayment(id);
        payment.setTotalPrice(dto.getTotalPrice());
        payment.setTransaction(getTransactionById(dto.getTransactionId()));

        Payment updated = paymentDao.save(payment);
        return convertToResponseDTO(updated);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = findActivePayment(id);
        payment.setDeleted(true);
        paymentDao.save(payment);
    }

     // Helper Methods
     private Payment findActivePayment(Long id) {
        return paymentDao.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Active Payment not found with ID: " + id));
    }

    private Transaction getTransactionById(Long id) {
        return transactionDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
    }

    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        PaymentResponseDTO dto = modelMapper.map(payment, PaymentResponseDTO.class);
        dto.setTransactionId(payment.getTransaction().getId());
        return dto;
    }
}
