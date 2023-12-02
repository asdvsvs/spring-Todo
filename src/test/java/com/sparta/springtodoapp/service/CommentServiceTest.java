package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.CommentRequestDto;
import com.sparta.springtodoapp.dto.CommentResponseDto;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.Users;
import com.sparta.springtodoapp.repository.CommentRepository;
import com.sparta.springtodoapp.repository.TodoRepository;
import com.sparta.springtodoapp.repository.UserRepository;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void writeComment() {
        //given
        Users user = new Users("sparta", "12345678");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        CommentRequestDto requestDto = new CommentRequestDto("coding",user.getUsername(),"club","game");
        CommentService commentService = new CommentService(commentRepository, todoRepository, userRepository);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(),user.getId())).willReturn(new Todo());

        //when
        CommentResponseDto responseDto =commentService.writeComment(userDetails, requestDto);

        //then
        assertEquals(requestDto.getContent(),responseDto.getContent());
    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}