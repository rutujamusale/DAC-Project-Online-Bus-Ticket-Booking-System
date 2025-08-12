package com.bus_ticket.dto.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private Long scheduleId;
    private String busName;
    private String source;
    private String destination;
    private String scheduleDate;
    private String departureTime;
    private String arrivalTime;
    private Double totalAmount;
    private String status;
    private LocalDateTime bookingDate;
    private String paymentStatus;
    private List<BookingSeatResponseDTO> bookingSeats;
    private List<PassengerResponseDTO> passengers;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerResponseDTO {
        private Long id;
        private String name;
        private Integer age;
        private String gender;
        private String contact;
        private String email;
        private String uid;
    }
}
