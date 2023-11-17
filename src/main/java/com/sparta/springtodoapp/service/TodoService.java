package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoListResponseDto;
import com.sparta.springtodoapp.dto.TodoRequestDto;

import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.User;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    public void makeTodo(UserDetailsImpl userDetails,TodoRequestDto requestDto) {
        String title = requestDto.getTitle();
        String content = requestDto.getContent();
        User user = userDetails.getUser();
        Todo todo = new Todo(title, content, user);
        todoRepository.save(todo);

    }

    public List<TodoListResponseDto> getTodoByUser() {
        long userCount =userRepository.count();
        List<TodoListResponseDto> responseDtoList = new ArrayList<>();

        for (long l=1;l<=userCount;l++){
            responseDtoList.addAll(todoRepository.findAllByUserIdIsOrderByCreatedAtDesc(l).stream().map(TodoListResponseDto::new).toList());
        }

        return responseDtoList;
    }
}
