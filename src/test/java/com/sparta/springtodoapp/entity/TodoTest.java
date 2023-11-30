package com.sparta.springtodoapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    @DisplayName("제목 내용 수정 테스트")
    void update() {
        //given
        String title = "수정한 제목";
        String content = "수정한 내용";

        //when
        Todo todo = new Todo();
        todo.update(title,content);

        //then
        assertEquals(title,todo.getTitle());
        assertEquals(content,todo.getContent());
    }

    @Test
    @DisplayName("완료처리 수정 테스트")
    void updateCompletion() {
        //given

        //when
        Todo todo = new Todo();
        todo.updateCompletion();

        //then
        assertEquals(true,todo.getCompletion());
    }
}