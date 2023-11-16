package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.UserRequestDto;
import com.sparta.springtodoapp.entity.User;
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

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자 이름이 존재");
        }
        User user = new User(username,password);
        userRepository.save(user);
    }
}

