package com.sparta.springtodoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {
    String title;
    String username;
    String content;
    String updateContent;
}
