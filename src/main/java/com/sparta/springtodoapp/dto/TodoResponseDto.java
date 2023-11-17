package com.sparta.springtodoapp.dto;

import com.sparta.springtodoapp.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public TodoResponseDto(Todo todo) {
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.username = todo.getUser().getUsername();
        this.createdAt = todo.getCreatedAt();
    }
}
