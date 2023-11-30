package com.sparta.springtodoapp.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("유저 생성자 테스트")
    void test1() {
        String username = "sparta";
        String password = "codingClub";
        User user = new User(username,password);

        assertEquals(username,user.getUsername());
        assertEquals(password,user.getPassword());
    }

}