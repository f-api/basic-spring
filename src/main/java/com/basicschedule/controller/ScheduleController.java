package com.basicschedule.controller;

import com.basicschedule.dto.ScheduleGetAllResponse;
import com.basicschedule.dto.ScheduleGetOneResponse;
import com.basicschedule.dto.ScheduleSaveRequest;
import com.basicschedule.dto.ScheduleSaveResponse;
import com.basicschedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<ScheduleSaveResponse> saveSchedule(
            @RequestBody ScheduleSaveRequest request
    ) {
        return ResponseEntity.ok(scheduleService.save(request));
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleGetOneResponse>> getSchedules(
            @RequestParam(required = false) String author
    ) {
        return ResponseEntity.ok(scheduleService.findSchedules(author));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleGetAllResponse> getSchedules(
            @PathVariable long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleGetOneResponse> updateSchedule(
            @PathVariable long scheduleId,
            @RequestBody ScheduleSaveRequest request
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, request));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public void deleteSchedule(
            @PathVariable long scheduleId,
            @RequestParam String password
    ) {
        scheduleService.deleteSchedule(scheduleId, password);
    }
}
