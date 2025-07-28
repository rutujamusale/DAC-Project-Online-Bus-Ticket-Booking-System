package com.bus_ticket.dto.Payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDTO {
    private Double totalPrice;
    private Long transactionId;
}
