package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoListResponseDto;
import com.sparta.springtodoapp.dto.TodoRequestDto;

import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoResponseDto makeTodo(UserDetailsImpl userDetails, TodoRequestDto requestDto) {
        String title = requestDto.getTitle();
        String content = requestDto.getContent();
        Users users = userDetails.getUser();
        Todo todo = new Todo(title, content, users);
        // 같은 유저의 같은 할일 중복 검사
        if (todoRepository.findByTitleAndUsersId(title, users.getId()) == null) {
            todoRepository.save(todo);
            return new TodoResponseDto(todo);
        } else {
            log.info("입력한 할일 제목이 이미 있습니다.");
            throw new IllegalArgumentException("입력한 할일 제목이 있습니다");
        }
    }

    public TodoResponseDto getTodoInfo(String title, String username) {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        Todo todo = todoRepository.findByTitleAndUsersId(title, users.getId());
        return new TodoResponseDto(todo);
    }

    public List<TodoListResponseDto> getTodos() {
        long userCount = userRepository.count();
        List<TodoListResponseDto> responseDtoList = new ArrayList<>();

        for (long l = 1; l <= userCount; l++) {
            responseDtoList.addAll(todoRepository.findAllByUsersIdIsOrderByCreatedAtDesc(l).stream().map(TodoListResponseDto::new).toList());
        }

        return responseDtoList;
    }

    @Transactional
    public TodoResponseDto updateTodo(UserDetailsImpl userDetails, String title, String username, TodoRequestDto requestDto) throws IllegalAccessException {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        Todo todo = new Todo();
        // 로그인 한 유저와 할일 작성 유저 일치 확인
        if (Objects.equals(userDetails.getUser().getUsername(), users.getUsername())) {
            todo =todoRepository.findByTitleAndUsersId(title, users.getId());
            todo.update(requestDto.getTitle(),requestDto.getContent());
        }else {
            throw new IllegalAccessException("로그인한 유저와 할일 작성자가 일치하지 않습니다");
        }
        return new TodoResponseDto(todo);
    }

    @Transactional
    public TodoResponseDto completeTodo(UserDetailsImpl userDetails, String title, String username) throws IllegalAccessException {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        Todo todo = new Todo();
        // 로그인 한 유저와 할일 작성 유저 일치 확인
        if (Objects.equals(userDetails.getUser().getUsername(), users.getUsername())) {
            todo =todoRepository.findByTitleAndUsersId(title, users.getId());
            todo.updateCompletion();
        }else {
            throw new IllegalAccessException("로그인한 유저와 할일 작성자가 일치하지 않습니다");
        }
        return new TodoResponseDto(todo);
    }
}
