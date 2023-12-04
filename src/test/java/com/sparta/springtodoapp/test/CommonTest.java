package com.sparta.springtodoapp.test;

import com.sparta.springtodoapp.entity.Users;

public interface CommonTest {

    String ANOTHER_PREFIX = "another-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "password";

    Users TEST_USER = Users.builder()
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();
    Users TEST_ANOTHER_USER = Users.builder()
            .username(ANOTHER_PREFIX + TEST_USER_NAME)
            .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
            .build();
}
