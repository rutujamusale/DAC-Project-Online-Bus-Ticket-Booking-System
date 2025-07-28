package com.bus_ticket.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.dao.BusDao;
import com.bus_ticket.dao.RouteDao;
import com.bus_ticket.dao.ScheduleDao;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dto.Schedule.NewScheduleDTO;
import com.bus_ticket.dto.Schedule.ScheduleResponseDTO;
import com.bus_ticket.entities.Schedule;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private final ScheduleDao scheduleDao;
    @Autowired
    private final RouteDao routeDao;
    @Autowired
    private final BusDao busDao;
    @Autowired
    private final VendorDao vendorDao;
    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public ScheduleResponseDTO createSchedule(NewScheduleDTO dto) {
        Schedule schedule = modelMapper.map(dto, Schedule.class);
        schedule.setRoute(routeDao.findById(dto.getRouteId()).orElseThrow());
        schedule.setBus(busDao.findById(dto.getBusId()).orElseThrow());
        schedule.setVendor(vendorDao.findById(dto.getVendorId()).orElseThrow());
        schedule.setDeleted(false);
        return modelMapper.map(scheduleDao.save(schedule), ScheduleResponseDTO.class);
    }

    @Override
    public ScheduleResponseDTO updateSchedule(Long id, NewScheduleDTO dto) {
        Schedule schedule = scheduleDao.findById(id).orElseThrow();
        schedule.setDepartureTime(dto.getDepartureTime());
        schedule.setArrivalTime(dto.getArrivalTime());
        schedule.setAvailableSeats(dto.getAvailableSeats());
        schedule.setRoute(routeDao.findById(dto.getRouteId()).orElseThrow());
        schedule.setBus(busDao.findById(dto.getBusId()).orElseThrow());
        schedule.setVendor(vendorDao.findById(dto.getVendorId()).orElseThrow());
        return modelMapper.map(scheduleDao.save(schedule), ScheduleResponseDTO.class);
    }

    @Override
    public ScheduleResponseDTO getScheduleById(Long id) {
        return modelMapper.map(scheduleDao.findById(id).orElseThrow(), ScheduleResponseDTO.class);
    }

    @Override
    public List<ScheduleResponseDTO> getAllSchedules() {
        return scheduleDao.findAll().stream()
                .filter(s -> !s.isDeleted())
                .map(schedule -> modelMapper.map(schedule, ScheduleResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void softDeleteSchedule(Long id) {
        Schedule schedule = scheduleDao.findById(id).orElseThrow();
        schedule.setDeleted(true);
        scheduleDao.save(schedule);
    }
}
