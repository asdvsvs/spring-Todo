package com.sparta.springtodoapp.repository;

import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void saveTodo() {
        //given
        Todo todo = new Todo("coding", "club", new Users("sparta", "123455678"));

        //when
        Todo savedtodo = todoRepository.save(todo);

        //then
        assertEquals(todo,savedtodo);
    }

    @Test
    void findAllByUsersIdIsOrderByCreatedAtDesc() {
        //given
        Users user1 = new Users("sparta", "12345678");
        Users user2 = new Users("spa","12345678");

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        todoRepository.save(new Todo("coding1","club1",user1));
        todoRepository.save(new Todo("coding2","club2",user1));
        todoRepository.save(new Todo("coding3","club3",user2));

        List<Todo> findTodo1 = todoRepository.findAllByUsersIdIsOrderByCreatedAtDesc(user1.getId());
        List<Todo> findTodo2 = todoRepository.findAllByUsersIdIsOrderByCreatedAtDesc(user2.getId());

        //then
        assertEquals(user1,findTodo1.get(0).getUsers());
        assertNotEquals(user1,findTodo2.get(0).getUsers());
    }

    @Test
    void findByTitleAndUsersId() {
        //given
        Users user = new Users("sparta", "12345678");
        Todo todo = new Todo("coding","club",user);

        //when
        userRepository.save(user);
        todoRepository.save(todo);
        Todo findTodo = todoRepository.findByTitleAndUsersId(todo.getTitle(),user.getId());

        //then
        assertEquals(todo,findTodo);
        assertEquals(todo.getUsers(),findTodo.getUsers());
    }
}