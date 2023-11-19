package com.sparta.springtodoapp.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    String title;
    String username;
    String content;
}
