package com.sparta.springtodoapp.controller;

import com.sparta.springtodoapp.dto.TodoListResponseDto;
import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j (topic = "Todo 컨트롤러")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/todo")
    public void makeTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoRequestDto requestDto){
        log.info("할일 생성");
        todoService.makeTodo(userDetails,requestDto);
    }

    @GetMapping("/todo")
    public TodoResponseDto getTodoInfo(String title, String username) {
        log.info("할일 카드 조회");
        return todoService.getTodoInfo(title, username);
    }

    @GetMapping("/todoList")
    public List<TodoListResponseDto> getTodoByUser() {
        log.info("할일 목록 조회");
        return todoService.getTodoByUser();
    }

    @PutMapping("/todo")
    public TodoResponseDto updateTodo(@AuthenticationPrincipal UserDetailsImpl userDetails,String title, String username, @RequestBody TodoRequestDto requestDto){
        log.info("할일 카드 수정");
        return todoService.updateTodo(userDetails,title,username,requestDto);
    }
}
