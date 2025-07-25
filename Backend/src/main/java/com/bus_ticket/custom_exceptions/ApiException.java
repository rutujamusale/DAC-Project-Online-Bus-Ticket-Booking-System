package com.bus_ticket.custom_exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiException extends RuntimeException {
    
    public ApiException(String message) {
        super(message);
    }
    
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
