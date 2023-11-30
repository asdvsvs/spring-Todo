package com.sparta.springtodoapp.controller;

import com.sparta.springtodoapp.dto.TodoListResponseDto;
import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j (topic = "Todo 컨트롤러")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/todo")
    public TodoResponseDto makeTodo(@RequestBody TodoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("할일 생성");
        return todoService.makeTodo(userDetails,requestDto);
    }

    @GetMapping("/todo")
    public TodoResponseDto getTodoInfo(String title, String username) {
        log.info("할일 카드 조회");
        return todoService.getTodoInfo(title, username);
    }

    @GetMapping("/todoList")
    public List<TodoListResponseDto> getTodos() {
        log.info("할일 목록 조회");
        return todoService.getTodos();
    }

    @PutMapping("/todo")
    public TodoResponseDto updateTodo(@AuthenticationPrincipal UserDetailsImpl userDetails,String title, String username, @RequestBody TodoRequestDto requestDto){
        log.info("할일 카드 수정");
        return todoService.updateTodo(userDetails,title,username,requestDto);
    }

    @PutMapping("/todo/completion")
    public TodoResponseDto completeTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, String title, String username){
        log.info("할일 완료 처리");
        return todoService.completeTodo(userDetails,title,username);
    }
}
