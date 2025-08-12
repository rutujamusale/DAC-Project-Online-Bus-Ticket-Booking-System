package com.bus_ticket.services;

import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.dao.PaymentDao;
import com.bus_ticket.dao.TransactionDao;
import com.bus_ticket.dao.BookingDao;
import com.bus_ticket.dao.TransactionBookingDao;
import com.bus_ticket.dao.SeatDao;
import com.bus_ticket.dao.BookingSeatDao;
import com.bus_ticket.dto.Payment.PaymentRequestDTO;
import com.bus_ticket.dto.Payment.PaymentResponseDTO;
import com.bus_ticket.entities.Payment;
import com.bus_ticket.entities.Transaction;
import com.bus_ticket.entities.Booking;
import com.bus_ticket.entities.TransactionBooking;
import com.bus_ticket.entities.Seat;
import com.bus_ticket.entities.BookingSeat;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private final PaymentDao paymentDao;

    @Autowired
    private final TransactionDao transactionDao;

    @Autowired
    private final BookingDao bookingDao;

    @Autowired
    private final TransactionBookingDao transactionBookingDao;

    @Autowired
    private final SeatDao seatDao;

    @Autowired
    private final BookingSeatDao bookingSeatDao;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        // This method is kept for backward compatibility
        return processPayment(dto);
    }

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payment request DTO cannot be null");
        }
        
        if (dto.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        
        if (dto.getPaymentMethod() == null || dto.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty");
        }

        try {
            // Get the booking
            Booking booking = bookingDao.findById(dto.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + dto.getBookingId()));

            // Validate booking status
            if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
                throw new IllegalStateException("Cannot process payment for cancelled booking");
            }

            // Create transaction
            Transaction transaction = new Transaction();
            transaction.setSchedule(booking.getSchedule());
            transaction.setUser(booking.getUser());
            transaction.setTotalAmount(booking.getTotalAmount());
            transaction.setPaymentMethod(dto.getPaymentMethod());
            transaction.setTransactionReference(generateTransactionReference());
            transaction.setPaymentStatus("PENDING");
            transaction.setActive(true);

            Transaction savedTransaction = transactionDao.save(transaction);

            // Create Payment record with enhanced details
            Payment.PaymentMethod paymentMethod = convertToPaymentMethod(dto.getPaymentMethod());
            Payment payment = new Payment(savedTransaction, booking.getTotalAmount(), paymentMethod);
            payment.setTransactionReference(savedTransaction.getTransactionReference());
            
            // Set additional payment details from DTO
            if (dto.getCardLastFourDigits() != null) {
                payment.setCardLastFourDigits(dto.getCardLastFourDigits());
            }
            if (dto.getCardType() != null) {
                payment.setCardType(dto.getCardType());
            }
            if (dto.getBankName() != null) {
                payment.setBankName(dto.getBankName());
            }
            if (dto.getUpiId() != null) {
                payment.setUpiId(dto.getUpiId());
            }
            if (dto.getWalletName() != null) {
                payment.setWalletName(dto.getWalletName());
            }
            
            // Save payment record
            Payment savedPayment = paymentDao.save(payment);

            // Create transaction booking relationship
            TransactionBooking transactionBooking = new TransactionBooking();
            transactionBooking.setTransaction(savedTransaction);
            transactionBooking.setBooking(booking);
            transactionBooking.setAmount(booking.getTotalAmount());
            transactionBooking.setStatus("PENDING");

            transactionBookingDao.save(transactionBooking);

            // Update booking with transaction
            booking.setTransaction(savedTransaction);
            booking.setPaymentStatus("PENDING");
            bookingDao.save(booking);

            // Simulate payment processing (in real scenario, integrate with payment gateway)
            boolean paymentSuccess = processPaymentWithGateway(dto);
            
            if (paymentSuccess) {
                // Update transaction and booking status
                savedTransaction.setPaymentStatus("COMPLETED");
                transactionDao.save(savedTransaction);
                
                transactionBooking.setStatus("COMPLETED");
                transactionBookingDao.save(transactionBooking);
                
                booking.setPaymentStatus("COMPLETED");
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingDao.save(booking);

                // Update Payment record with success details
                savedPayment.markAsCompleted(
                    generateGatewayTransactionId(),
                    "SUCCESS",
                    "Payment processed successfully"
                );
                paymentDao.save(savedPayment);

                // Update seats from RESERVED → BOOKED after successful payment
                List<BookingSeat> bookingSeats = booking.getBookingSeats();
                if (bookingSeats != null && !bookingSeats.isEmpty()) {
                    for (BookingSeat bookingSeat : bookingSeats) {
                        if (bookingSeat.getSeat() != null) {
                            Seat seat = bookingSeat.getSeat();
                            seat.setStatus(Seat.SeatStatus.BOOKED);
                            seatDao.save(seat);
                        }
                    }
                }

                return new PaymentResponseDTO(
                    savedTransaction.getId(),
                    booking.getId(),
                    "COMPLETED",
                    dto.getPaymentMethod(),
                    savedTransaction.getTransactionReference(),
                    booking.getTotalAmount(),
                    LocalDateTime.now(),
                    "Payment processed successfully",
                    true
                );
            } else {
                // Payment failed
                savedTransaction.setPaymentStatus("FAILED");
                transactionDao.save(savedTransaction);
                
                transactionBooking.setStatus("FAILED");
                transactionBookingDao.save(transactionBooking);
                
                booking.setPaymentStatus("FAILED");
                booking.setStatus(Booking.BookingStatus.PAYMENT_FAILED);
                bookingDao.save(booking);

                // Update Payment record with failure details
                savedPayment.markAsFailed(
                    "Payment gateway processing failed",
                    "FAILED",
                    "Payment processing failed"
                );
                paymentDao.save(savedPayment);

                // Release seats back to AVAILABLE if payment fails
                List<BookingSeat> bookingSeats = booking.getBookingSeats();
                if (bookingSeats != null && !bookingSeats.isEmpty()) {
                    for (BookingSeat bookingSeat : bookingSeats) {
                        if (bookingSeat.getSeat() != null) {
                            Seat seat = bookingSeat.getSeat();
                            seat.setStatus(Seat.SeatStatus.AVAILABLE);
                            seatDao.save(seat);
                        }
                    }
                }

                return new PaymentResponseDTO(
                    savedTransaction.getId(),
                    booking.getId(),
                    "FAILED",
                    dto.getPaymentMethod(),
                    savedTransaction.getTransactionReference(),
                    booking.getTotalAmount(),
                    LocalDateTime.now(),
                    "Payment processing failed",
                    false
                );
            }

        } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException e) {
            // Re-throw business exceptions as-is
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error processing payment: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Payment ID cannot be null");
        }
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
    public List<PaymentResponseDTO> getPaymentsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        List<Transaction> transactions = transactionDao.findByUserId(userId);
        return transactions.stream()
                .map(this::convertTransactionToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("Payment ID cannot be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Payment request DTO cannot be null");
        }
        
        Payment payment = findActivePayment(id);
        // Update logic would go here - for now just return the existing payment
        Payment updated = paymentDao.save(payment);
        return convertToResponseDTO(updated);
    }

    @Override
    public void deletePayment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Payment ID cannot be null");
        }
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

    private String generateTransactionReference() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateGatewayTransactionId() {
        return "GTW" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private boolean processPaymentWithGateway(PaymentRequestDTO dto) {
        // Simulate payment gateway processing
        // In real implementation, integrate with actual payment gateway
        try {
            Thread.sleep(1000); // Simulate processing time
            return Math.random() > 0.1; // 90% success rate for demo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    private Payment.PaymentMethod convertToPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return Payment.PaymentMethod.UPI; // Default to UPI
        }
        
        try {
            return Payment.PaymentMethod.valueOf(paymentMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to UPI if method is not recognized
            return Payment.PaymentMethod.UPI;
        }
    }

    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        
        return new PaymentResponseDTO(
            payment.getTransaction().getId(),
            null, // bookingId would need to be fetched from transactionBookings
            payment.getPaymentStatus().name(),
            payment.getPaymentMethod().name(),
            payment.getTransactionReference(),
            payment.getTotalPrice(),
            payment.getPaymentDate(),
            payment.getGatewayResponseMessage(),
            Payment.PaymentStatus.COMPLETED.equals(payment.getPaymentStatus())
        );
    }

    private PaymentResponseDTO convertTransactionToResponseDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        return new PaymentResponseDTO(
            transaction.getId(),
            null, // bookingId would need to be fetched from transactionBookings
            transaction.getPaymentStatus(),
            transaction.getPaymentMethod(),
            transaction.getTransactionReference(),
            transaction.getTotalAmount(),
            transaction.getCreatedAt(),
            "Transaction processed",
            "COMPLETED".equals(transaction.getPaymentStatus())
        );
    }
}
