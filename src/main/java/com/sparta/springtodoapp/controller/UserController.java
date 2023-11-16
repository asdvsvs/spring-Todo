package com.sparta.springtodoapp.controller;

import com.sparta.springtodoapp.dto.UserRequestDto;
import com.sparta.springtodoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(!fieldErrors.isEmpty()){
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + "필드 : "+ fieldError.getDefaultMessage());
            }
        }
        else {
            userService.signup(requestDto);
        }
    }

}
