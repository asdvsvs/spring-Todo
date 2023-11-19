package com.sparta.springtodoapp.repository;

import com.sparta.springtodoapp.dto.TodoResponseDto;
import com.sparta.springtodoapp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {

    List<Todo> findAllByUserIdIsOrderByCreatedAtDesc(long l);

    Todo findByTitleAndUserId(String title, Long id);

    List<Todo> findAllByTitle(String title);
}
