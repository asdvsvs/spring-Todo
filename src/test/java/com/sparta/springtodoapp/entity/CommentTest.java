package com.sparta.springtodoapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("내용 수정 테스트")
    void update() {
        //given
        String content = "수정수정";

        //when
        Comment comment = new Comment();
        comment.update(content);

        //then
        assertEquals(content, comment.getContent());
    }
}