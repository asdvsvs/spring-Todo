package com.sparta.springtodoapp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "users")
    private List<Todo> todos = new ArrayList<>();
    @OneToMany(mappedBy = "users")
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
