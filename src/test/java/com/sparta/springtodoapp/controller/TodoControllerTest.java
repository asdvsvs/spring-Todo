package com.sparta.springtodoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springtodoapp.config.WebSecurityConfig;
import com.sparta.springtodoapp.dto.TodoRequestDto;
import com.sparta.springtodoapp.entity.User;
import com.sparta.springtodoapp.jwt.JwtUtil;
import com.sparta.springtodoapp.mvc.MockSpringSecurityFilter;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.service.TodoService;
import com.sparta.springtodoapp.service.UserService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        User testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
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
}