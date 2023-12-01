package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.User;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void makeTodo() {
        //given
        UserDetailsImpl userDetails =new UserDetailsImpl(new User());
        TodoRequestDto requestDto= new TodoRequestDto("","");
        TodoService todoService = new TodoService(todoRepository,userRepository);

        //when
        given(todoRepository.findByTitleAndUserId(requestDto.getTitle(),new User().getId())).willReturn(new Todo());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()->todoService.makeTodo(userDetails,requestDto)
        );

        given(todoRepository.findByTitleAndUserId(requestDto.getTitle(),new User().getId())).willReturn(null);
        todoService.makeTodo(userDetails,requestDto);

        //then
        assertEquals("입력한 할일 제목이 있습니다",exception.getMessage());
    }

    @Test
    void getTodoInfo() {
        //given
        String title = "spring";
        String username = "sparta";
        TodoService todoService = new TodoService(todoRepository,userRepository);
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        //when
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                ()->todoService.getTodoInfo(title,username)
        );

        //then
        assertEquals("Not Found "+username,exception.getMessage());
    }

    @Test
    void getTodos() {
    }

    @Test
    void updateTodo() {
    }

    @Test
    void completeTodo() {
    }
}