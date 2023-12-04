package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.UserRequestDto;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.test.CommonTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest implements CommonTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원가입")
    class signup{
        @Test
        @DisplayName("회원가입 성공")
        void signup_success() {
            //given
            UserRequestDto requestDto = new UserRequestDto(TEST_USER_NAME, TEST_USER_PASSWORD);
            Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
            UserService userService = new UserService(userRepository, passwordEncoder);
            given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());

            //when
            userService.signup(requestDto);

            //then

        }

        @Test
        @DisplayName("회원가입 실패")
        void signup_fail() {
            //given
            UserRequestDto requestDto = new UserRequestDto(TEST_USER_NAME, TEST_USER_PASSWORD);
            Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
            UserService userService = new UserService(userRepository, passwordEncoder);
            given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(users));

            //when
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.signup(requestDto)
            );

            //then
            assertEquals("중복된 사용자 이름입니다", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("로그인")
    class login{
        @Test
        @DisplayName("로그인 성공")
        void login_success() {
            //given
            UserRequestDto requestDto = new UserRequestDto(TEST_USER_NAME, TEST_USER_PASSWORD);
            Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
            UserService userService = new UserService(userRepository, passwordEncoder);
            given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(users));
            given(passwordEncoder.matches(requestDto.getPassword(), users.getPassword())).willReturn(true);

            //when
            userService.login(requestDto);

            //then

        }

        @Test
        @DisplayName("로그인 실패 - 존재하지 않는 유저")
        void login_fail_suchUser() {
            //given
            UserRequestDto requestDto = new UserRequestDto(TEST_USER_NAME, TEST_USER_PASSWORD);
            Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
            UserService userService = new UserService(userRepository, passwordEncoder);
            given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());

            //when
            NullPointerException nullPointerException = assertThrows(
                    NullPointerException.class,
                    ()-> userService.login(requestDto)
            );

            //then
            assertEquals("등록된 유저가 없습니다.",nullPointerException.getMessage());

        }

        @Test
        @DisplayName("로그인 실패 - 비밀번호 불일치")
        void login_fail_password() {
            //given
            UserRequestDto requestDto = new UserRequestDto(TEST_USER_NAME, TEST_USER_PASSWORD);
            Users users = new Users(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
            UserService userService = new UserService(userRepository, passwordEncoder);
            given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(users));
            given(passwordEncoder.matches(requestDto.getPassword(), users.getPassword())).willReturn(false);

            //when
            IllegalArgumentException illegalArgumentException = assertThrows(
                    IllegalArgumentException.class,
                    ()-> userService.login(requestDto)
            );

            //then
            assertEquals("비밀번호가 일치하지 않습니다.",illegalArgumentException.getMessage());
        }

    }
}