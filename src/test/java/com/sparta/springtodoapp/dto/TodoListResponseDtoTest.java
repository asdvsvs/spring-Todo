package com.sparta.springtodoapp.dto;

import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoListResponseDtoTest {

    @Test
    @DisplayName("생성자 테스트")
    void test1() {
        //given
        String username= "스파르타";
        String password = "";
        String title= "몰입하자";
        String content ="";
        User user = new User(username, password);
        Todo todo =new Todo(title,content,user);

        //when
        TodoListResponseDto todoListResponseDto = new TodoListResponseDto(todo);

        //then
        assertEquals(username,todoListResponseDto.getUsername());
        assertEquals(title, todoListResponseDto.getTitle());
        assertEquals(false, todoListResponseDto.getCompletion());
        assertEquals(null,todoListResponseDto.getCreatedAt());
    }

}