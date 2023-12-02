package com.sparta.springtodoapp.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentResponseDtoTest {

    @Test
    @DisplayName("생성자 테스트")
    void test1() {
        //given
        String content = "내용내용";

        //when
        CommentResponseDto commentResponseDto = new CommentResponseDto(content);

        //then
        assertEquals(content, commentResponseDto.getContent());
    }

}