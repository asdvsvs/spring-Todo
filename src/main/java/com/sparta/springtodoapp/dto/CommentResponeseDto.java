package com.sparta.springtodoapp.dto;

import lombok.Getter;

@Getter
public class CommentResponeseDto {
    String content;

    public CommentResponeseDto(String content) {
        this.content = content;
    }
}
