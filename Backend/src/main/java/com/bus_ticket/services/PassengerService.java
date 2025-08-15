package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.entities.Passenger;

public interface PassengerService {
    Passenger createPassenger(Passenger passenger);
    Passenger getPassengerById(Long id);
    List<Passenger> getAllPassengers();
    List<Passenger> getPassengersByBookingId(Long bookingId);
    Passenger updatePassenger(Long id, Passenger updatedPassenger);
    void deletePassenger(Long id);
}