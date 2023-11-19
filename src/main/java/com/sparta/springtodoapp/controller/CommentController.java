package com.sparta.springtodoapp.controller;

import com.sparta.springtodoapp.dto.CommentRequestDto;
import com.sparta.springtodoapp.dto.CommentResponeseDto;
import com.sparta.springtodoapp.security.UserDetailsImpl;
import com.sparta.springtodoapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "Comment 컨트롤러")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponeseDto writeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        log.info("댓글 작성");
        return commentService.writeComment(userDetails, requestDto);
    }

    @PutMapping("/comment")
    public void updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(userDetails,requestDto);
    }
}
