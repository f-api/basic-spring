package com.basicschedule.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String author;
    private String password;
    private Long scheduleId;

    public Comment(String content, String author, String password, Long scheduleId) {
        this.content = content;
        this.author = author;
        this.password = password;
        this.scheduleId = scheduleId;
    }
}