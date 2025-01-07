package com.jwtauthentication.jwtauthsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles = new ArrayList<>();

    public Permission() {}

    public Permission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Permission{id=" + id + ", name='" + name + "'}";
    }
}
