package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.CommentRequestDto;
import com.sparta.springtodoapp.dto.CommentResponseDto;
import com.sparta.springtodoapp.entity.Comment;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
        CommentRequestDto requestDto = new CommentRequestDto("coding", user.getUsername(), "club", "game");
        CommentService commentService = new CommentService(commentRepository, todoRepository, userRepository);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), user.getId())).willReturn(new Todo());

        //when -then

        CommentResponseDto responseDto = commentService.writeComment(userDetails, requestDto);

        assertThrows(IllegalArgumentException.class, () -> commentService.writeComment(null, requestDto));

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> commentService.writeComment(userDetails, requestDto));
        assertEquals(requestDto.getContent(), responseDto.getContent());

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        when(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), user.getId())).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> commentService.writeComment(userDetails, requestDto));
    }

    @Test
    void updateComment() {
        //given
        Users user = new Users("sparta", "12345678");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Comment comment = new Comment("beforeUpdate", user, new Todo());
        CommentRequestDto requestDto = new CommentRequestDto("coding", user.getUsername(), "beforeUpdate", "AfterUpdate");
        CommentService commentService = new CommentService(commentRepository, todoRepository, userRepository);
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(user));
        given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), user.getId())).willReturn(new Todo());
        given(commentRepository.findByContentAndUsersId(requestDto.getContent(), user.getId())).willReturn(comment);

        //when - then
        assertNotEquals(comment.getContent(), requestDto.getUpdateContent());

        commentService.updateComment(userDetails, requestDto);
        assertEquals(comment.getContent(), requestDto.getUpdateContent());

    }

    @Test
    void deleteComment() {
        //given
        Users user = new Users("sparta", "12345678");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Comment comment = new Comment("beforeUpdate", user, new Todo());
        CommentRequestDto requestDto = new CommentRequestDto("coding", user.getUsername(), "beforeUpdate", "AfterUpdate");
        CommentService commentService = new CommentService(commentRepository, todoRepository, userRepository);
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(user));
        given(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), user.getId())).willReturn(new Todo());
        given(commentRepository.findByContentAndUsersId(requestDto.getContent(), user.getId())).willReturn(comment);

        //when - then
        commentService.deleteComment(userDetails, requestDto);
        given(commentRepository.findByContentAndUsersId(requestDto.getContent(), user.getId())).willReturn(null);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> commentService.deleteComment(userDetails, requestDto)
        );
        assertEquals("해당 댓글은 존재하지 않습니다.", exception.getMessage());

        assertThrows(IllegalArgumentException.class, () -> commentService.deleteComment(null, requestDto));

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> commentService.deleteComment(userDetails, requestDto));

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        when(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), user.getId())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> commentService.deleteComment(userDetails, requestDto));

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(commentRepository.findByContentAndUsersId(requestDto.getContent(), user.getId())).willReturn(comment);
        when(todoRepository.findByTitleAndUsersId(requestDto.getTitle(), user.getId())).thenReturn(new Todo());
        assertThrows(IllegalArgumentException.class, () -> commentService.deleteComment(
                new UserDetailsImpl(new Users("스파르타","12345678")),
                requestDto
                )
        );
    }
}