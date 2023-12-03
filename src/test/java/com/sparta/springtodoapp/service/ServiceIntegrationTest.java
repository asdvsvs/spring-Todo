package com.sparta.springtodoapp.service;


import com.sparta.springtodoapp.dto.*;
import com.sparta.springtodoapp.entity.Comment;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.CommentRepository;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ServiceIntegrationTest {

    @Autowired
    TodoService todoService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    TodoRepository todoRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    String username = "sparta";
    String password = "12345678";

    Users user;
    UserDetailsImpl userDetails;
    TodoResponseDto createTodo;
    TodoResponseDto updatedTodo;
    CommentResponseDto createComment;
    CommentResponseDto updatedComment;

    @Test
    @Order(1)
    @DisplayName("유저 생성")
    void test1() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto(username, password);

        //when
        userService.signup(userRequestDto);
        Users findUser = userRepository.findById(1L).orElseThrow();

        //then
        assertNotNull(findUser.getId());
        assertEquals("sparta", findUser.getUsername());
        user = findUser;
    }

    @Test
    @Order(2)
    @DisplayName("로그인")
    void test2() {
        //given
        UserRequestDto requestDto = new UserRequestDto(username, password);

        //when-then
        userService.login(requestDto);
    }

    @Test
    @Order(3)
    @DisplayName("할일 등록")
    void test3() {
        //given
        TodoRequestDto todoRequestDto = new TodoRequestDto("coding", "club");
        userDetails = new UserDetailsImpl(user);

        //when
        TodoResponseDto todo = todoService.makeTodo(userDetails, todoRequestDto);

        //then
        assertNotNull(todo.getCreatedAt());
        assertEquals("coding", todo.getTitle());
        assertEquals("club", todo.getContent());
        createTodo = todo;
    }

    @Test
    @Order(4)
    @DisplayName("할일 조회")
    void test4() {
        //given
        String title = createTodo.getTitle();
        String username = user.getUsername();

        //when
        TodoResponseDto todoResponseDto = todoService.getTodoInfo(title, username);

        //then
        assertNotNull(todoResponseDto);
        assertEquals(title, todoResponseDto.getTitle());
        assertEquals(username, todoResponseDto.getUsername());
    }

    @Test
    @Order(5)
    @DisplayName("할일 전체 조회")
    void test5() {
        //given
        //when
        List<TodoListResponseDto> list = todoService.getTodos();
        TodoListResponseDto todoListResponseDto = list.get(0);

        //then
        assertEquals(createTodo.getTitle(), todoListResponseDto.getTitle());
        assertEquals(createTodo.getUsername(), todoListResponseDto.getUsername());
    }

    @Test
    @Order(6)
    @DisplayName("할일 수정")
    void test6() throws IllegalAccessException {
        //given
        TodoRequestDto todoRequestDto = new TodoRequestDto("수정된 할일", "수정된 내용");

        //when
        TodoResponseDto responseDto = todoService.updateTodo(userDetails, createTodo.getTitle(), createTodo.getUsername(), todoRequestDto);

        //then
        assertEquals(todoRequestDto.getTitle(), responseDto.getTitle());
        assertEquals(todoRequestDto.getContent(), responseDto.getContent());
        updatedTodo = responseDto;
    }

    @Test
    @Order(7)
    @DisplayName("할일 완료처리")
    void test7() throws IllegalAccessException {
        //given
        String title = updatedTodo.getTitle();
        String username = updatedTodo.getUsername();

        //when
        todoService.completeTodo(userDetails, title, username);
        Todo todo = todoRepository.findById(1L).orElseThrow();

        //then
        assertEquals(true, todo.getCompletion());
    }

    @Test
    @Order(8)
    @DisplayName("댓글 작성")
    void test8() {
        //given
        String content = "댓글 내용";
        CommentRequestDto requestDto = new CommentRequestDto(updatedTodo.getTitle(), updatedTodo.getUsername(), content, "");

        //when
        CommentResponseDto responseDto = commentService.writeComment(userDetails, requestDto);

        //then
        assertEquals(content, responseDto.getContent());
        createComment = responseDto;
    }

    @Test
    @Order(9)
    @DisplayName("댓글 수정")
    void test9() {
        //given
        String content = createComment.getContent();
        String newContent = "수정한 댓글 내용";
        CommentRequestDto requestDto = new CommentRequestDto(updatedTodo.getTitle(), updatedTodo.getUsername(), content, newContent);

        //when
        commentService.updateComment(userDetails, requestDto);
        Comment comment = commentRepository.findById(1L).orElseThrow();
        //then
        assertEquals(newContent, comment.getContent());
        updatedComment = new CommentResponseDto(comment.getContent());
    }

    @Test
    @Order(10)
    @DisplayName("할일 삭제")
    void test10() {
        //given
        String content = updatedComment.getContent();
        String newContent = "";
        CommentRequestDto requestDto = new CommentRequestDto(updatedTodo.getTitle(), updatedTodo.getUsername(), content, newContent);

        //when
        commentService.deleteComment(userDetails, requestDto);

        //then
        assertThrows(
                NoSuchElementException.class,
                () -> commentRepository.findById(1L).orElseThrow()
        );
    }
}
