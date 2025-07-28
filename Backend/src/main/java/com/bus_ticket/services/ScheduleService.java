package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.Schedule.NewScheduleDTO;
import com.bus_ticket.dto.Schedule.ScheduleResponseDTO;

public interface ScheduleService {
    ScheduleResponseDTO createSchedule(NewScheduleDTO dto);
    ScheduleResponseDTO updateSchedule(Long id, NewScheduleDTO dto);
    ScheduleResponseDTO getScheduleById(Long id);
    List<ScheduleResponseDTO> getAllSchedules();
    void softDeleteSchedule(Long id);
}