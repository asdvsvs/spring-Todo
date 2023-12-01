package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.dto.TodoResponseDto;
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

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
        User user = new User(username,"");
        TodoService todoService = new TodoService(todoRepository,userRepository);

        //when
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                ()->todoService.getTodoInfo(title,username)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(todoRepository.findByTitleAndUserId(title,user.getId())).willReturn(new Todo(title,"",user));
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
        User user = new User(username,"");
        Todo todo = new Todo(title,content,user);
        UserDetailsImpl userDetails = new UserDetailsImpl(new User());
        TodoRequestDto todoRequestDto = new TodoRequestDto(title,content);
        TodoService todoService = new TodoService(todoRepository,userRepository);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User(username,"")));
        given(todoRepository.findByTitleAndUserId(title,user.getId())).willReturn(todo);

        //when
        IllegalAccessException exception = assertThrows(
                IllegalAccessException.class,
                ()-> todoService.updateTodo(userDetails,title,username, todoRequestDto)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User()));
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
        User user = new User(username,"");
        Todo todo = new Todo(title,content,user);
        UserDetailsImpl userDetails = new UserDetailsImpl(new User("",""));
        TodoService todoService = new TodoService(todoRepository,userRepository);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User(username,"")));
        given(todoRepository.findByTitleAndUserId(title,user.getId())).willReturn(todo);

        //when
        IllegalAccessException exception = assertThrows(
                IllegalAccessException.class,
                ()-> todoService.completeTodo(userDetails,title,username)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User("","")));
        TodoResponseDto responseDto = todoService.completeTodo(userDetails,title,username);

        //then
        assertEquals("로그인한 유저와 할일 작성자가 일치하지 않습니다",exception.getMessage());
        assertEquals(true,responseDto.getCompletion());
    }
}