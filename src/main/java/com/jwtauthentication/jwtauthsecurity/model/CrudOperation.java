package com.jwtauthentication.jwtauthsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "crud")
@Setter
@Getter
public class CrudOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String createBy;
    private LocalDateTime createdAt;
    private String text;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
