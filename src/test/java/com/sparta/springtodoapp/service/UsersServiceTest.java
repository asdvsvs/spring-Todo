package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.UserRequestDto;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void signup() {
        //given
        UserRequestDto requestDto = new UserRequestDto("sparta", "codingClub");
        Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
        UserService userService = new UserService(userRepository, passwordEncoder);

        //when
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(users));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signup(requestDto)
        );

        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());
        userService.signup(requestDto);

        //then
        assertEquals("중복된 사용자 이름입니다", exception.getMessage());
    }

    @Test
    void login() {
        //given
        UserRequestDto requestDto = new UserRequestDto("sparta", "codingClub");
        Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
        UserService userService = new UserService(userRepository, passwordEncoder);

        //when
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());
        NullPointerException nullPointerException = assertThrows(
                NullPointerException.class,
                ()-> userService.login(requestDto)
        );
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(users));
        given(passwordEncoder.matches(requestDto.getPassword(), users.getPassword())).willReturn(false);
        IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                ()-> userService.login(requestDto)
        );
        given(passwordEncoder.matches(requestDto.getPassword(), users.getPassword())).willReturn(true);
        userService.login(requestDto);


        //then
        assertEquals("등록된 유저가 없습니다.",nullPointerException.getMessage());
        assertEquals("비밀번호가 일치하지 않습니다.",illegalArgumentException.getMessage());
    }
}