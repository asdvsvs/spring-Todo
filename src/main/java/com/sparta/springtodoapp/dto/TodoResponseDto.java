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
    private LocalDateTime modifiedAt;

    public TodoResponseDto(Todo todo) {
        this.username = todo.getUser().getUsername();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.completion = todo.getCompletion();
        this.createdAt = todo.getCreatedAt();
        this.modifiedAt = todo.getModifiedAt();
    }
}
