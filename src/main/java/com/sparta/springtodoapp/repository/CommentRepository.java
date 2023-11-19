package com.sparta.springtodoapp.repository;

import com.sparta.springtodoapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByContentAndUserId(String content, Long id);
}
