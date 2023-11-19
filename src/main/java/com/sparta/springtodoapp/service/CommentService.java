package com.sparta.springtodoapp.service;

import com.sparta.springtodoapp.dto.CommentRequestDto;
import com.sparta.springtodoapp.dto.CommentResponeseDto;
import com.sparta.springtodoapp.entity.Comment;
import com.sparta.springtodoapp.entity.Todo;
import com.sparta.springtodoapp.entity.User;
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

    public CommentResponeseDto writeComment(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        if(userDetails ==null) throw new IllegalArgumentException("로그인이 필요합니다");
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not Found " + requestDto.getUsername()));
        Todo todo = todoRepository.findByTitleAndUserId(requestDto.getTitle(), user.getId());
        if (todo !=null) {
            CommentResponeseDto responseDto = new CommentResponeseDto(requestDto.getContent());
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
        if(userDetails ==null) throw new IllegalArgumentException("로그인이 필요합니다");
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not Found " + requestDto.getUsername()));
        Comment comment =commentRepository.findByContentAndUserId(requestDto.getContent(),user.getId());
        if(Objects.equals(userDetails.getUser().getUsername(), requestDto.getUsername())){
            comment.update(requestDto.getUpdateContent());
        }

    }
}
