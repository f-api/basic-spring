package com.basicschedule.service;

import com.basicschedule.dto.*;
import com.basicschedule.entity.Comment;
import com.basicschedule.entity.Schedule;
import com.basicschedule.repository.CommentRepository;
import com.basicschedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

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
    public List<ScheduleGetOneResponse> findSchedules(String author) {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleGetOneResponse> dtos = new ArrayList<>();

        if (author == null) {
            for (Schedule schedule : schedules) {
                ScheduleGetOneResponse scheduleGetOneResponse = new ScheduleGetOneResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getAuthor(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                );
                dtos.add(scheduleGetOneResponse);
            }
            return dtos;
        }
        for (Schedule schedule : schedules) {
            if (author.equals(schedule.getAuthor())) {
                ScheduleGetOneResponse scheduleGetOneResponse = new ScheduleGetOneResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getAuthor(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                );
                dtos.add(scheduleGetOneResponse);
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
    public ScheduleGetAllResponse findSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found")
        );
        
        List<Comment> comments = commentRepository.findByScheduleId(scheduleId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        
        return new ScheduleGetAllResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                commentResponses
        );
    }

    @Transactional
    public ScheduleGetOneResponse updateSchedule(long scheduleId, ScheduleSaveRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found")
        );
        if (ObjectUtils.nullSafeEquals(schedule.getPassword(), request.getPassword())) {
           throw new IllegalStateException("Password doesn't match");
        }
        schedule.updateTitleAndAuthor(request.getTitle(), request.getAuthor());
        return new ScheduleGetOneResponse(
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
