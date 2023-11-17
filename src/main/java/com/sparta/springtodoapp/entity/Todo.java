package com.sparta.springtodoapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Todo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;
    private Boolean completion;

    public Todo(String title, String content, User user) {
        this.user=user;
        this.title=title;
        this.content=content;
        this.completion =false;
    }
}
