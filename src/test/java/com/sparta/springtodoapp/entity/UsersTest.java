package com.sparta.springtodoapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    @Test
    @DisplayName("유저 생성자 테스트")
    void test1() {
        String username = "sparta";
        String password = "codingClub";
        Users users = new Users(username,password);

        assertEquals(username, users.getUsername());
        assertEquals(password, users.getPassword());
    }

}