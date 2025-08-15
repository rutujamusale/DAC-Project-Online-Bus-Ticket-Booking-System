package com.bus_ticket.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.bus_ticket.dao.PassengerDao;
import com.bus_ticket.entities.Passenger;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerDao passengerDao;

    @Override
    public Passenger createPassenger(Passenger passenger) {
        if (passengerDao.existsByUid(passenger.getUid())) {
            throw new IllegalArgumentException("Passenger with UID already exists.");
        }
        return passengerDao.save(passenger);
    }

    @Override
    public Passenger getPassengerById(Long id) {
        return passengerDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + id));
    }

    @Override
    public List<Passenger> getAllPassengers() {
        return passengerDao.findAll();
    }

    @Override
    public List<Passenger> getPassengersByBookingId(Long bookingId) {
        return passengerDao.findByBookingId(bookingId);
    }

    @Override
    public Passenger updatePassenger(Long id, Passenger updatedPassenger) {
        Passenger existing = getPassengerById(id);
        existing.setName(updatedPassenger.getName());
        existing.setAge(updatedPassenger.getAge());
        existing.setGender(updatedPassenger.getGender());
        existing.setContact(updatedPassenger.getContact());
        existing.setEmail(updatedPassenger.getEmail());
        existing.setUid(updatedPassenger.getUid());
        return passengerDao.save(existing);
    }

    @Override
    public void deletePassenger(Long id) {
        Passenger passenger = getPassengerById(id);
        passengerDao.delete(passenger);
    }
}
