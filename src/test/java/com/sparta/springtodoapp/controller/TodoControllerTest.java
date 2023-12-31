package com.sparta.springtodoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springtodoapp.config.WebSecurityConfig;
import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.jwt.JwtUtil;
import com.sparta.springtodoapp.mvc.MockSpringSecurityFilter;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {TodoController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@ActiveProfiles("test")
class TodoControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TodoService todoService;

    @MockBean
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        String username = "sparta";
        String password = "codingClub";
        Users testUsers = new Users(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUsers);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails,"");
    }

    @Test
    @DisplayName("할일 생성 테스트")
    void makeTodo() throws Exception {
        //given
        this.mockUserSetup();
        String title = "spring";
        String content = "sparta";
        TodoRequestDto todoRequestDto = new TodoRequestDto(title, content);
        String postInfo = objectMapper.writeValueAsString(todoRequestDto);

        //when-then
        mvc.perform(post("/api/todo")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("할일 카드 조회")
    void getTodoInfo() throws Exception {
        //given
        String title = "spring";
        String username = "sparta";

        //when-then
        mvc.perform(get("/api/todo")
                        .param("title",title)
                        .param("username",username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("할일 목록 조회")
    void getTodos() throws Exception {
        //when-then
        mvc.perform(get("/api/todoList"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("할일 카드 수정")
    void updateTodo() throws Exception {
        //given
        this.mockUserSetup();
        String title = "spring";
        String username = "sparta";
        String newTitle = "spring";
        String content = "sparta";
        TodoRequestDto todoRequestDto = new TodoRequestDto(newTitle, content);
        String postInfo = objectMapper.writeValueAsString(todoRequestDto);


        //when-then
        mvc.perform(put("/api/todo")
                        .param("title", title)
                        .param("username", username)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("할일 완료 처리")
    void completeTodo() throws Exception {
        //given
        String title = "spring";
        String username = "sparta";

        //when-then
        mvc.perform(put("/api/todo/completion")
                        .param("title", title)
                        .param("username", username)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}