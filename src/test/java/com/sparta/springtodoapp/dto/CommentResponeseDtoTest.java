package com.sparta.springtodoapp.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentResponeseDtoTest {

    @Test
    @DisplayName("생성자 테스트")
    void test1() {
        //given
        String content = "내용내용";

        //when
        CommentResponeseDto commentResponeseDto = new CommentResponeseDto(content);

        //then
        assertEquals(content,commentResponeseDto.getContent());
    }

}