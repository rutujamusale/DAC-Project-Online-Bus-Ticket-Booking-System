package com.bus_ticket.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus_ticket.dao.ScheduleDao;
import com.bus_ticket.dao.BusDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Schedule.ScheduleDto;
import com.bus_ticket.entities.Schedule;
import com.bus_ticket.entities.Bus;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    
    @Autowired
    private ScheduleDao scheduleDao;
    
    @Autowired
    private BusDao busDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private SeatInitializationService seatInitializationService;
    
    @Override
    public ApiResponse addSchedule(ScheduleDto scheduleDto) {
        Bus bus = busDao.findById(scheduleDto.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + scheduleDto.getBusId()));
        
        // Check if schedule already exists for this bus and date
        if (scheduleDao.findByBusIdAndDate(scheduleDto.getBusId(), scheduleDto.getScheduleDate()).isPresent()) {
            throw new ApiException("Schedule already exists for this bus on " + scheduleDto.getScheduleDate());
        }
        
        // Validate source and destination are different
        if (scheduleDto.getSource().equalsIgnoreCase(scheduleDto.getDestination())) {
            throw new ApiException("Source and destination cannot be the same");
        }
        
        Schedule schedule = new Schedule();
        schedule.setBus(bus);
        schedule.setSource(scheduleDto.getSource());
        schedule.setDestination(scheduleDto.getDestination());
        schedule.setScheduleDate(scheduleDto.getScheduleDate());
        schedule.setDepartureTime(scheduleDto.getDepartureTime());
        schedule.setArrivalTime(scheduleDto.getArrivalTime());
        schedule.setAvailableSeats(scheduleDto.getAvailableSeats() != null ? 
                                   scheduleDto.getAvailableSeats() : bus.getTotalSeats());
        
        scheduleDao.save(schedule);
        
        // Initialize seats for this schedule
        seatInitializationService.initializeSeatsForSchedule(schedule);
        
        return new ApiResponse("Schedule added successfully");
    }
    
    @Override
    public List<ScheduleDto> getSchedulesByBusId(Long busId) {
        return scheduleDao.findByBusId(busId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ScheduleDto> getSchedulesByVendorId(Long vendorId) {
        return scheduleDao.findByVendorId(vendorId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ScheduleDto> searchScheduledBuses(String source, String destination, LocalDate date) {
        return scheduleDao.findScheduledBuses(source, destination, date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public ScheduleDto getScheduleById(Long id) {
        Schedule schedule = scheduleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        if (!schedule.isActive()) {
            throw new ResourceNotFoundException("Schedule not found with id: " + id);
        }
        
        return convertToDto(schedule);
    }
    
    @Override
    public ApiResponse updateSchedule(Long id, ScheduleDto scheduleDto) {
        Schedule schedule = scheduleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        if (!schedule.isActive()) {
            throw new ResourceNotFoundException("Schedule not found with id: " + id);
        }
        
        // Validate source and destination are different
        if (scheduleDto.getSource().equalsIgnoreCase(scheduleDto.getDestination())) {
            throw new ApiException("Source and destination cannot be the same");
        }
        
        schedule.setSource(scheduleDto.getSource());
        schedule.setDestination(scheduleDto.getDestination());
        schedule.setScheduleDate(scheduleDto.getScheduleDate());
        schedule.setDepartureTime(scheduleDto.getDepartureTime());
        schedule.setArrivalTime(scheduleDto.getArrivalTime());
        schedule.setAvailableSeats(scheduleDto.getAvailableSeats());
        
        scheduleDao.save(schedule);
        return new ApiResponse("Schedule updated successfully");
    }
    
    @Override
    public ApiResponse deleteSchedule(Long id) {
        Schedule schedule = scheduleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        // Hard delete the schedule
        scheduleDao.delete(schedule);
        return new ApiResponse("Schedule deleted successfully");
    }
    
    private ScheduleDto convertToDto(Schedule schedule) {
        ScheduleDto dto = new ScheduleDto();
        dto.setId(schedule.getId());
        dto.setBusId(schedule.getBus().getId());
        dto.setSource(schedule.getSource());
        dto.setDestination(schedule.getDestination());
        dto.setScheduleDate(schedule.getScheduleDate());
        dto.setDepartureTime(schedule.getDepartureTime());
        dto.setArrivalTime(schedule.getArrivalTime());
        dto.setAvailableSeats(schedule.getAvailableSeats());
        dto.setActive(schedule.isActive());
        
        // Add bus details
        Bus bus = schedule.getBus();
        dto.setBusName(bus.getBusName());
        dto.setBusNumber(bus.getBusNumber());
        dto.setPrice(bus.getPrice().toString());
        dto.setBusType(bus.getBusType());
        
        return dto;
    }
}
