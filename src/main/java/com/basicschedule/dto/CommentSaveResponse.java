package com.basicschedule.dto;

import com.basicschedule.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentSaveResponse {
    private final Long id;
    private final String content;
    private final String author;
    private final Long scheduleId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentSaveResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getAuthor();
        this.scheduleId = comment.getScheduleId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getModifiedAt();
    }
}
