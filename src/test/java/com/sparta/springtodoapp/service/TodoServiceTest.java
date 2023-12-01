package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
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
        UserDetailsImpl userDetails =new UserDetailsImpl(new Users());
        TodoRequestDto requestDto= new TodoRequestDto("","");
        TodoService todoService = new TodoService(todoRepository,userRepository);

        //when
        given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(),new Users().getId())).willReturn(new Todo());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()->todoService.makeTodo(userDetails,requestDto)
        );

        given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(),new Users().getId())).willReturn(null);
        todoService.makeTodo(userDetails,requestDto);

        //then
        assertEquals("입력한 할일 제목이 있습니다",exception.getMessage());
    }

    @Test
    void getTodoInfo() {
        //given
        String title = "spring";
        String username = "sparta";
        Users users = new Users(username,"");
        TodoService todoService = new TodoService(todoRepository,userRepository);

        //when
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                ()->todoService.getTodoInfo(title,username)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(users));
        given(todoRepository.findByTitleAndUsersId(title, users.getId())).willReturn(new Todo(title,"", users));
        TodoResponseDto responseDto = todoService.getTodoInfo(title,username);

        //then
        assertEquals("Not Found "+username,exception.getMessage());
        assertEquals(title,responseDto.getTitle());
        assertEquals(username, responseDto.getUsername());
    }

    @Test
    void getTodos() {
    }

    @Test
    void updateTodo() throws IllegalAccessException {
        //given
        String username = "sparta";
        String title ="spring";
        String content = "codingClub";
        Users users = new Users(username,"");
        Todo todo = new Todo(title,content, users);
        UserDetailsImpl userDetails = new UserDetailsImpl(new Users());
        TodoRequestDto todoRequestDto = new TodoRequestDto(title,content);
        TodoService todoService = new TodoService(todoRepository,userRepository);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new Users(username,"")));
        given(todoRepository.findByTitleAndUsersId(title, users.getId())).willReturn(todo);

        //when
        IllegalAccessException exception = assertThrows(
                IllegalAccessException.class,
                ()-> todoService.updateTodo(userDetails,title,username, todoRequestDto)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new Users()));
        TodoResponseDto responseDto = todoService.updateTodo(userDetails,title,username,todoRequestDto);

        //then
        assertEquals("로그인한 유저와 할일 작성자가 일치하지 않습니다",exception.getMessage());
        assertEquals(username,responseDto.getUsername());
        assertEquals(title,responseDto.getTitle());
        assertEquals(content,responseDto.getContent());
    }

    @Test
    void completeTodo() throws IllegalAccessException {
        //given
        String username = "sparta";
        String title ="spring";
        String content = "codingClub";
        Users users = new Users(username,"");
        Todo todo = new Todo(title,content, users);
        UserDetailsImpl userDetails = new UserDetailsImpl(new Users("",""));
        TodoService todoService = new TodoService(todoRepository,userRepository);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new Users(username,"")));
        given(todoRepository.findByTitleAndUsersId(title, users.getId())).willReturn(todo);

        //when
        IllegalAccessException exception = assertThrows(
                IllegalAccessException.class,
                ()-> todoService.completeTodo(userDetails,title,username)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new Users("","")));
        TodoResponseDto responseDto = todoService.completeTodo(userDetails,title,username);

        //then
        assertEquals("로그인한 유저와 할일 작성자가 일치하지 않습니다",exception.getMessage());
        assertEquals(true,responseDto.getCompletion());
    }
}