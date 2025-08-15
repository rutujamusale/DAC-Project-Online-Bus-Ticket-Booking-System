package com.bus_ticket.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus_ticket.dao.BusDao;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dao.ScheduleDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusDto;
import com.bus_ticket.entities.Bus;
import com.bus_ticket.entities.Vendor;
import com.bus_ticket.entities.Schedule;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class BusServiceImpl implements BusService {
    
    private static final Logger logger = LoggerFactory.getLogger(BusServiceImpl.class);
    
    @Autowired
    private BusDao busDao;
    
    @Autowired
    private VendorDao vendorDao;
    
    @Autowired
    private ScheduleDao scheduleDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public ApiResponse addBus(BusDto busDto) {
        // Check if bus number already exists
        if (busDao.findByBusNumber(busDto.getBusNumber()).isPresent()) {
            throw new ApiException("Bus with number " + busDto.getBusNumber() + " already exists");
        }
        
        Vendor vendor = vendorDao.findById(busDto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + busDto.getVendorId()));
        
        if (vendor.isDeleted()) {
            throw new ApiException("Cannot add bus for inactive vendor");
        }
        
        Bus bus = new Bus();
        bus.setBusNumber(busDto.getBusNumber());
        bus.setBusName(busDto.getBusName());
        bus.setTotalSeats(busDto.getTotalSeats());
        bus.setAvailableSeats(busDto.getTotalSeats());
        bus.setPrice(busDto.getPrice());
        bus.setBusType(busDto.getBusType());
        bus.setVendor(vendor);
        
        busDao.save(bus);
        return new ApiResponse("Bus added successfully");
    }
    
    @Override
    public List<BusDto> getBusesByVendor(Long vendorId) {
        logger.info("Fetching buses for vendor: {}", vendorId);
        
        try {
            List<Bus> buses = busDao.findByVendorId(vendorId);
            logger.info("Found {} buses for vendor {}", buses.size(), vendorId);
            
            List<BusDto> busDtos = buses.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            
            logger.debug("Converted {} buses to DTOs", busDtos.size());
            return busDtos;
            
        } catch (Exception e) {
            logger.error("Error fetching buses for vendor: {}", vendorId, e);
            throw e;
        }
    }
    
    @Override
    @Transactional
    public ApiResponse deleteBus(Long id) {
        logger.info("=== STARTING HARD DELETE PROCESS ===");
        logger.info("Attempting to hard delete bus with ID: {}", id);
        
        try {
            logger.info("Step 1: Finding bus in database...");
            Bus bus = busDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
            
            logger.info("Step 2: Bus found - ID: {}, Number: {}, Name: {}", 
                       bus.getId(), bus.getBusNumber(), bus.getBusName());
            
            logger.info("Step 3: Finding schedules for bus...");
            List<Schedule> busSchedules = scheduleDao.findAllByBusId(id);
            logger.info("Found {} schedules for bus {}", busSchedules.size(), id);
            
            // Delete all schedules for this bus first (due to foreign key constraints)
            logger.info("Step 4: Deleting all schedules for bus...");
            int deletedSchedules = 0;
            for (Schedule schedule : busSchedules) {
                logger.info("Deleting schedule: ID={}, Source={}, Destination={}", 
                           schedule.getId(), schedule.getSource(), schedule.getDestination());
                scheduleDao.delete(schedule);
                deletedSchedules++;
            }
            
            if (deletedSchedules > 0) {
                scheduleDao.flush(); // Ensure schedule deletions are persisted
                logger.info("Step 5: Flushed {} schedule deletions to database", deletedSchedules);
            }
            
            // Now delete the bus
            logger.info("Step 6: Deleting the bus...");
            busDao.delete(bus);
            busDao.flush(); // Ensure bus deletion is persisted
            
            logger.info("=== HARD DELETE PROCESS COMPLETED ===");
            logger.info("Successfully hard deleted bus {} and {} schedules", id, deletedSchedules);
            return new ApiResponse(true, "Bus and associated schedules deleted successfully");
            
        } catch (ResourceNotFoundException e) {
            logger.error("=== HARD DELETE FAILED - BUS NOT FOUND ===");
            logger.error("Bus not found: {}", id, e);
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            logger.error("=== HARD DELETE FAILED - UNEXPECTED ERROR ===");
            logger.error("Error hard deleting bus: {}", id, e);
            e.printStackTrace();
            return new ApiResponse(false, "Error deleting bus: " + e.getMessage());
        }
    }
    
    private BusDto convertToDto(Bus bus) {
        BusDto dto = new BusDto();
        dto.setId(bus.getId());
        dto.setBusNumber(bus.getBusNumber());
        dto.setBusName(bus.getBusName());
        dto.setTotalSeats(bus.getTotalSeats());
        dto.setAvailableSeats(bus.getAvailableSeats());
        dto.setPrice(bus.getPrice());
        dto.setBusType(bus.getBusType());
        dto.setVendorId(bus.getVendor().getId());
        return dto;
    }
}
