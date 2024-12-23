package com.jwtauthentication.jwtauthsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    private String createBy;
    private LocalDateTime createdAt;
    private String text;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
