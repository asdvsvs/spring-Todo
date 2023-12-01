package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.UserRequestDto;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

        public void signup(UserRequestDto requestDto) {
            String username = requestDto.getUsername();
            String password = passwordEncoder.encode(requestDto.getPassword());

            Optional<Users> checkUsername = userRepository.findByUsername(username);
            if (checkUsername.isPresent()){
                throw new IllegalArgumentException("중복된 사용자 이름입니다");
            }
            Users users = new Users(username,password);
        userRepository.save(users);
    }

    public void login(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("등록된 유저가 없습니다."));

        if(!passwordEncoder.matches(password, users.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}

