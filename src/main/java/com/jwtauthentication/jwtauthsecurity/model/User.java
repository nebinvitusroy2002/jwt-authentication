package com.jwtauthentication.jwtauthsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@Setter
@Accessors(chain = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true,length = 150,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false,name = "created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Getter
    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
