package com.basicschedule.service;

import com.basicschedule.dto.ScheduleResponse;
import com.basicschedule.dto.ScheduleSaveRequest;
import com.basicschedule.dto.ScheduleSaveResponse;
import com.basicschedule.entity.Schedule;
import com.basicschedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleSaveResponse save(ScheduleSaveRequest request) {
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getAuthor(),
                request.getPassword()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleSaveResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getAuthor(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findSchedules(String author) {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponse> dtos = new ArrayList<>();

        if (author == null) {
            for (Schedule schedule : schedules) {
                ScheduleResponse scheduleResponse = new ScheduleResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getAuthor(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                );
                dtos.add(scheduleResponse);
            }
            return dtos;
        }
        for (Schedule schedule : schedules) {
            if (author.equals(schedule.getAuthor())) {
                ScheduleResponse scheduleResponse = new ScheduleResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getAuthor(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                );
                dtos.add(scheduleResponse);
            }
        }
        return dtos;

        /**
         * 아래와 같은 방법들도 가능합니다(이번 주차에서 배운 내용을 넘어서지만요).
         *
         * @Transactional(readOnly = true)
         * public List<ScheduleResponse> findSchedules(String author) {
         *     List<Schedule> schedules;
         *
         *     if (author == null) {
         *         schedules = scheduleRepository.findAll();
         *     } else {
         *         schedules = scheduleRepository.findByAuthor(author);
         *     }
         *
         *     return schedules.stream()
         *             .map(schedule -> new ScheduleResponse(...))
         *             .toList();
         * }
         *
         *
         * ---
         *
         * public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
         *
         *     @Query("SELECT s FROM Schedule s WHERE :author IS NULL OR s.author = :author")
         *     List<Schedule> findSchedulesByAuthorIfProvided(@Param("author") String author);
         *
         * }
         */
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found")
        );
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public ScheduleResponse updateSchedule(long scheduleId, ScheduleSaveRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found")
        );
        if (ObjectUtils.nullSafeEquals(schedule.getPassword(), request.getPassword())) {
           throw new IllegalStateException("Password doesn't match");
        }
        schedule.updateTitleAndAuthor(request.getTitle(), request.getAuthor());
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public void deleteSchedule(long scheduleId, String password) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found")
        );
        if (!ObjectUtils.nullSafeEquals(schedule.getPassword(), password)) {
            throw new IllegalStateException("Password doesn't match");
        }
        scheduleRepository.deleteById(scheduleId);
    }
}
