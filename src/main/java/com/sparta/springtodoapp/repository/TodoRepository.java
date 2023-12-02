package com.sparta.springtodoapp.repository;

import com.sparta.springtodoapp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {

    List<Todo> findAllByUsersIdIsOrderByCreatedAtDesc(long l);

    Todo findByTitleAndUsersId(String title, Long id);

}
