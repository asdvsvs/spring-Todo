package com.sparta.springtodoapp.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    String content;

    public CommentResponseDto(String content) {
        this.content = content;
    }
}
