package com.basicschedule.repository;

import com.basicschedule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByScheduleId(Long scheduleId);
    long countByScheduleId(Long scheduleId);
}