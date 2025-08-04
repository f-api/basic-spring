package com.basicschedule.controller;

import com.basicschedule.dto.CommentSaveRequest;
import com.basicschedule.dto.CommentSaveResponse;
import com.basicschedule.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentSaveResponse> saveComment(
            @PathVariable Long scheduleId,
            @RequestBody CommentSaveRequest request
    ) {
        return ResponseEntity.ok(commentService.saveComment(scheduleId, request));
    }
}
