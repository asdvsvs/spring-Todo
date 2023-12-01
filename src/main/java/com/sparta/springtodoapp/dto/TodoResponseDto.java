package com.sparta.springtodoapp.dto;

import com.sparta.springtodoapp.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private String username;
    private String title;
    private String content;
    private Boolean completion;
    private LocalDateTime createdAt;

    public TodoResponseDto(Todo todo) {
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.username = todo.getUsers().getUsername();
        this.completion = todo.getCompletion();
        this.createdAt = todo.getCreatedAt();
    }
}
