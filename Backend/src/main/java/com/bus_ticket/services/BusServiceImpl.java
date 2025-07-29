package com.bus_ticket.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus_ticket.dao.BusDao;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusDto;
import com.bus_ticket.dto.Bus.BusSearchRequest;
import com.bus_ticket.entities.Bus;
import com.bus_ticket.entities.Vendor;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BusServiceImpl implements BusService {
    
    @Autowired
    private BusDao busDao;
    
    @Autowired
    private VendorDao vendorDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public ApiResponse addBus(BusDto busDto) {
        // Check if bus number already exists
        if (busDao.findByBusNumber(busDto.getBusNumber()).isPresent()) {
            throw new ApiException("Bus with number " + busDto.getBusNumber() + " already exists");
        }
        
        // Get vendor
        Vendor vendor = vendorDao.findById(busDto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + busDto.getVendorId()));
        
        if (vendor.isDeleted()) {
            throw new ApiException("Cannot add bus for inactive vendor");
        }
        
        Bus bus = new Bus();
        bus.setBusNumber(busDto.getBusNumber());
        bus.setBusName(busDto.getBusName());
        bus.setTotalSeats(busDto.getTotalSeats());
        bus.setAvailableSeats(busDto.getTotalSeats()); // Initially all seats are available
        bus.setPrice(busDto.getPrice());
        bus.setBusType(busDto.getBusType());
        bus.setVendor(vendor);
        
        busDao.save(bus);
        return new ApiResponse("Bus added successfully");
    }
    
    @Override
    public List<BusDto> getAllActiveBuses() {
        return busDao.findAllBuses()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BusDto> getBusesByVendor(Long vendorId) {
        return busDao.findByVendorId(vendorId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public BusDto getBusById(Long id) {
        Bus bus = busDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        
        return convertToDto(bus);
    }
    
    @Override
    public ApiResponse updateBus(Long id, BusDto busDto) {
        Bus bus = busDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        
        // Check if bus number is being changed and if it already exists
        if (!bus.getBusNumber().equals(busDto.getBusNumber())) {
            if (busDao.findByBusNumber(busDto.getBusNumber()).isPresent()) {
                throw new ApiException("Bus with number " + busDto.getBusNumber() + " already exists");
            }
        }
        
        // Update bus details
        bus.setBusNumber(busDto.getBusNumber());
        bus.setBusName(busDto.getBusName());
        bus.setTotalSeats(busDto.getTotalSeats());
        bus.setPrice(busDto.getPrice());
        bus.setBusType(busDto.getBusType());
        
        busDao.save(bus);
        return new ApiResponse("Bus updated successfully");
    }
    
    @Override
    public ApiResponse softDeleteBus(Long id) {
        Bus bus = busDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        
        // Hard delete - this will also delete all associated schedules due to cascade
        busDao.delete(bus);
        return new ApiResponse("Bus deleted successfully");
    }
    
    @Override
    public List<BusDto> searchBuses(BusSearchRequest searchRequest) {
        // This method is no longer used as search is now done through schedules
        return getAllActiveBuses();
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
