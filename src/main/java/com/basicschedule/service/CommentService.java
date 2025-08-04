package com.basicschedule.service;

import com.basicschedule.dto.CommentSaveRequest;
import com.basicschedule.dto.CommentSaveResponse;
import com.basicschedule.entity.Comment;
import com.basicschedule.repository.CommentRepository;
import com.basicschedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CommentSaveResponse saveComment(Long scheduleId, CommentSaveRequest request) {
        boolean isScheduleExist = scheduleRepository.existsById(scheduleId);
        if (!isScheduleExist) {
            throw new IllegalStateException("Schedule does not exist");
        }

        long commentCount = commentRepository.countByScheduleId(scheduleId);
        if (commentCount >= 10) {
            throw new IllegalStateException("하나의 일정에는 댓글을 10개까지만 작성할 수 있습니다.");
        }

        if (request.getAuthor() == null) {
            throw new IllegalStateException("작성자명은 필수값입니다.");
        }
        if (request.getPassword() == null) {
            throw new IllegalStateException("비밀번호는 필수값입니다.");
        }
        if (request.getContent() == null) {
            throw new IllegalStateException("댓글 내용은 필수값입니다.");
        }

        if (request.getContent().length() > 100) {
            throw new IllegalStateException("댓글 내용은 최대 100자입니다.");
        }

        Comment comment = new Comment(
                request.getContent(),
                request.getAuthor(),
                request.getPassword(),
                scheduleId
        );

        Comment savedComment = commentRepository.save(comment);
        return new CommentSaveResponse(savedComment);
    }
}
