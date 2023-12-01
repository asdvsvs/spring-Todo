package com.sparta.springtodoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springtodoapp.config.WebSecurityConfig;
import com.sparta.springtodoapp.dto.CommentRequestDto;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.jwt.JwtUtil;
import com.sparta.springtodoapp.mvc.MockSpringSecurityFilter;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.service.CommentService;
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
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@ActiveProfiles("test")
class CommentControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

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
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "");
    }

    @Test
    @DisplayName("댓글 작성")
    void writeComment() throws Exception {
        //given
        this.mockUserSetup();
        String title = "spring";
        String username = "sparta";
        String content = "codingClub";
        String updateContent = "";
        CommentRequestDto requestDto = new CommentRequestDto(title, username, content, updateContent);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when-then
        mvc.perform(post("/api/comment")
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateComment() throws Exception{
        //given
        this.mockUserSetup();
        String title = "spring";
        String username = "sparta";
        String content = "codingClub";
        String updateContent = "CookingClub";
        CommentRequestDto requestDto = new CommentRequestDto(title, username, content, updateContent);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when-then
        mvc.perform(put("/api/comment")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteComment() throws Exception{
        //given
        this.mockUserSetup();
        String title = "spring";
        String username = "sparta";
        String content = "codingClub";
        String updateContent = "";
        CommentRequestDto requestDto = new CommentRequestDto(title, username, content, updateContent);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when-then
        mvc.perform(delete("/api/comment")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}