package com.sparta.springtodoapp.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private String content;

    public Comment(String content, User user, Todo todo) {
        this.content = content;
        this.user = user;
        this.todo =todo;
    }
}
