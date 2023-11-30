package com.sparta.springtodoapp.dto;

import com.sparta.springtodoapp.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TodoRequestDto {
    private String title;
    private String content;
}
