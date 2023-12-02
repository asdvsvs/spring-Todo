package com.sparta.springtodoapp.repository;

import com.sparta.springtodoapp.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsersRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser(){
        //given
        Users users = new Users("sparta","12345678");

        //when
        Users savedUsers = userRepository.save(users);

        //then
        assertNotNull(savedUsers);
        assertEquals(users.getUsername(), savedUsers.getUsername());
        assertEquals(users.getPassword(),savedUsers.getPassword());
    }

    @Test
    void findByUsername() {
        //given
        Users users = new Users("sparta","12345678");

        //when
        userRepository.save(users);
        Users findUsers = userRepository.findByUsername(users.getUsername()).orElseThrow();

        //then
        assertEquals(users,findUsers);
    }
}