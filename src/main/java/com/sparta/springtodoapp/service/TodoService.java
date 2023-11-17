package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.User;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    public void makeTodo(UserDetailsImpl userDetails,TodoRequestDto requestDto) {
        String title = requestDto.getTitle();
        String content = requestDto.getContent();
        User user = userDetails.getUser();
        Todo todo = new Todo(title, content, user);
        todoRepository.save(todo);

    }
}
