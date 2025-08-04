package com.basicschedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequest {
    private String content;
    private String author;
    private String password;
}