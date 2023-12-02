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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public CommentResponseDto writeComment(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        if(userDetails ==null) throw new IllegalArgumentException("로그인이 필요합니다");
        Users users = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not Found " + requestDto.getUsername()));
        Todo todo = todoRepository.findByTitleAndUsersId(requestDto.getTitle(), users.getId());
        if (todo !=null) {
            CommentResponseDto responseDto = new CommentResponseDto(requestDto.getContent());
            Comment comment = new Comment(requestDto.getContent(),userDetails.getUser(), todo);
            commentRepository.save(comment);
            return responseDto;
        } else{
            log.info("할일 존재 X");
            throw new IllegalArgumentException("해당 할일은 존재하지 않습니다.");
        }
    }

    @Transactional
    public void updateComment(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        Comment comment = checkComment(userDetails, requestDto);
        comment.update(requestDto.getUpdateContent());
    }

    public void deleteComment(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        Comment comment = checkComment(userDetails, requestDto);
        commentRepository.deleteById(comment.getId());
    }

    private Comment checkComment(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        if(userDetails ==null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        Users users = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not Found " + requestDto.getUsername()));
        if(!Objects.equals(userDetails.getUser().getUsername(), requestDto.getUsername())){
            throw new IllegalArgumentException("로그인 한 유저와 댓글 작성자 불일치");
        }
        return commentRepository.findByContentAndUsersId(requestDto.getContent(), users.getId());
    }
}
