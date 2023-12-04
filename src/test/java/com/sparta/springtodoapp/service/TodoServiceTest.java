package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.TodoListResponseDto;
import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.test.CommonTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest implements CommonTest {
    @Mock
    TodoRepository todoRepository;

    @Mock
    UserRepository userRepository;


    @Nested
    @DisplayName("할일 생성")
    class makeTodo {
        @Test
        @DisplayName("할일 생성 성공")
        void makeTodo_success() {
            //given
            UserDetailsImpl userDetails = new UserDetailsImpl(TEST_USER);
            TodoRequestDto requestDto = new TodoRequestDto(TEST_TITLE, TEST_CONTENT);
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), new Users().getId())).willReturn(null);

            //when
            TodoResponseDto todoResponseDto = todoService.makeTodo(userDetails, requestDto);

            //then
            assertNotNull(todoResponseDto);
            assertEquals(userDetails.getUser().getUsername(), todoResponseDto.getUsername());
            assertEquals(requestDto.getTitle(), todoResponseDto.getTitle());
        }

        @Test
        @DisplayName("할일 생성 실패 - 할일 제목 중복")
        void makeTodo_fail_same_title() {
            //given
            UserDetailsImpl userDetails = new UserDetailsImpl(TEST_USER);
            TodoRequestDto requestDto = new TodoRequestDto(TEST_TITLE, TEST_CONTENT);
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), new Users().getId())).willReturn(new Todo());

            //when
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> todoService.makeTodo(userDetails, requestDto)
            );

            //then
            assertEquals("입력한 할일 제목이 있습니다", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("할일 단건 조회")
    class getTodoInfo {
        @Test
        @DisplayName("할일 단건 조회 성공")
        void getTodoInfo_success() {
            //given
            String title = TEST_TITLE;
            String username = TEST_USER_NAME;
            Users users = TEST_USER;
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(userRepository.findByUsername(username)).willReturn(Optional.of(users));
            given(todoRepository.findByTitleAndUsersId(title, users.getId())).willReturn(new Todo(title, TEST_CONTENT, users));

            //when
            TodoResponseDto responseDto = todoService.getTodoInfo(title, username);

            //then
            assertEquals(title, responseDto.getTitle());
            assertEquals(username, responseDto.getUsername());
        }

        @Test
        @DisplayName("할일 단건 조회 실패 - 해당 유저 없음")
        void getTodoInfo_fail() {
            //given
            String title = TEST_TITLE;
            String username = TEST_USER_NAME;
            Users users = TEST_USER;
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(userRepository.findByUsername(username)).willReturn(Optional.empty());

            //when
            UsernameNotFoundException exception = assertThrows(
                    UsernameNotFoundException.class,
                    () -> todoService.getTodoInfo(title, username)
            );

            //then
            assertEquals("Not Found " + username, exception.getMessage());
        }
    }


    @Test
    void getTodos() {
        //given
        Users users = new Users();
        List<Todo> todoList = new ArrayList<>();
        todoList.add(new Todo("1번 제목", "1번 내용", users));
        todoList.add(new Todo("2번 제목", "2번 내용", users));
        TodoService todoService = new TodoService(todoRepository, userRepository);
        given(userRepository.count()).willReturn(2L);
        given(todoRepository.findAllByUsersIdIsOrderByCreatedAtDesc(TEST_USER_ID)).willReturn(todoList);

        //when
        List<TodoListResponseDto> responseDto = todoService.getTodos();

        //then
        assertEquals(todoList.get(0).getTitle(), responseDto.get(0).getTitle());
        assertEquals(todoList.get(1).getTitle(), responseDto.get(1).getTitle());

    }

    @Nested
    @DisplayName("할일 수정")
    class updateTodo {

        @Test
        @DisplayName("할일 수정 성공")
        void updateTodo_success() throws IllegalAccessException {
            //given
            Todo todo = new Todo(TEST_TITLE, TEST_CONTENT, TEST_USER);
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(userRepository.findByUsername(TEST_USER_NAME)).willReturn(Optional.of(TEST_USER));
            given(todoRepository.findByTitleAndUsersId(TEST_TITLE, TEST_USER.getId())).willReturn(todo);

            //when
            TodoResponseDto responseDto = todoService.updateTodo(new UserDetailsImpl(TEST_USER), TEST_TITLE, TEST_USER_NAME, new TodoRequestDto(ANOTHER_TEST_TITLE, ANOTHER_TEST_CONTENT));

            //then
            assertEquals(TEST_USER_NAME, responseDto.getUsername());
            assertEquals(ANOTHER_TEST_TITLE, responseDto.getTitle());
            assertEquals(ANOTHER_TEST_CONTENT, responseDto.getContent());
        }

        @Test
        @DisplayName("할일 수정 실패 - 작성자 불일치")
        void updateTodo_fail_author() throws IllegalAccessException {
            //given
            Todo todo = new Todo(TEST_TITLE, TEST_CONTENT, TEST_USER);
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(userRepository.findByUsername(TEST_USER_NAME)).willReturn(Optional.of(TEST_USER));

            //when
            IllegalAccessException exception = assertThrows(
                    IllegalAccessException.class,
                    () -> todoService.updateTodo(new UserDetailsImpl(TEST_ANOTHER_USER), TEST_TITLE, TEST_USER_NAME, new TodoRequestDto(ANOTHER_TEST_TITLE, ANOTHER_TEST_CONTENT))
            );

            //then
            assertEquals("로그인한 유저와 할일 작성자가 일치하지 않습니다", exception.getMessage());
        }

        @Test
        @DisplayName("할일 수정 실패 - 해당 유저 없음")
        void updateTodo_fail_() throws IllegalAccessException {
            //given
            Todo todo = new Todo(TEST_TITLE, TEST_CONTENT, TEST_USER);
            TodoService todoService = new TodoService(todoRepository, userRepository);
            given(userRepository.findByUsername(TEST_USER_NAME)).willReturn(Optional.empty());

            //when-then
            assertThrows(
                    UsernameNotFoundException.class,
                    () -> todoService.updateTodo(new UserDetailsImpl(TEST_USER), TEST_TITLE, TEST_USER_NAME, new TodoRequestDto(ANOTHER_TEST_TITLE, ANOTHER_TEST_CONTENT))
            );
        }

    }


    @Test
    void completeTodo() throws IllegalAccessException {
        //given
        String username = "sparta";
        String title = "spring";
        String content = "codingClub";
        Users users = new Users(username, "");
        Todo todo = new Todo(title, content, users);
        UserDetailsImpl userDetails = new UserDetailsImpl(new Users("", ""));
        TodoService todoService = new TodoService(todoRepository, userRepository);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new Users(username, "")));
        given(todoRepository.findByTitleAndUsersId(title, users.getId())).willReturn(todo);

        //when
        IllegalAccessException exception = assertThrows(
                IllegalAccessException.class,
                () -> todoService.completeTodo(userDetails, title, username)
        );
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(
                UsernameNotFoundException.class,
                () -> todoService.completeTodo(userDetails, title, username)
        );
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new Users("", "")));
        TodoResponseDto responseDto = todoService.completeTodo(userDetails, title, username);

        //then
        assertEquals("로그인한 유저와 할일 작성자가 일치하지 않습니다", exception.getMessage());
        assertEquals(true, responseDto.getCompletion());
    }
}