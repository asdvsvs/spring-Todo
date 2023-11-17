package com.sparta.springtodoapp.dto;

import com.sparta.springtodoapp.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoListResponseDto {
    private String username;
    private String title;
    private Boolean completion;
    private LocalDateTime createdAt;

    public TodoListResponseDto(Todo todo) {
        this.username = todo.getUser().getUsername();
        this.title = todo.getTitle();
        this.completion = todo.getCompletion();
        this.createdAt = todo.getCreatedAt();
    }
}
