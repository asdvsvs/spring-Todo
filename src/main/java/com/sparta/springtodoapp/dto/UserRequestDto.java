package com.sparta.springtodoapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @NotNull
    @Size(min = 4, max = 10, message = "4~10글자 사이로 입력")
    @Pattern(regexp = "^[a-z0-9]*$",message = "a~z, 0~9만 가능")
    private String username;

    @NotNull
    @Size(min = 8, max = 15, message = "8~15글자 사이로 입력")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "a~z, A~Z, 0~9만 가능")
    private String password;
}
