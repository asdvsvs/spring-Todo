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
    public void makeTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoRequestDto requestDto){
        log.info("할일 생성");
        todoService.makeTodo(userDetails,requestDto);
    }

//    @PostMapping("/todo")
//    public void getTodoInfo() {
//
//    }

    @GetMapping("/todo")
    public List<TodoListResponseDto> getTodoByUser() {
        log.info("할일 목록 조회");
        return todoService.getTodoByUser();
    }
}
