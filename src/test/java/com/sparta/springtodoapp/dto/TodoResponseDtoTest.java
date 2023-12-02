package com.sparta.springtodoapp.dto;

import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseDtoTest {

    @Test
    @DisplayName("생성자 테스트")
    void test1() {
        //given
        Todo todo = new Todo("coding", "club", new Users("sparta", "12345678"));

        //when
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        //then
        assertEquals(todo.getTitle(),todoResponseDto.getTitle());
        assertEquals(todo.getUsers().getUsername(),todoResponseDto.getUsername());
    }

}