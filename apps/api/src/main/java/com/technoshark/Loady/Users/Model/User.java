package com.technoshark.Loady.Users.Model;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.technoshark.Loady.Enums.RoleEnums;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User { 

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "username", nullable = true)
    private String username;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "sign_in_provider", nullable = true)
    private String signInProvider;

    // @Column(nullable = false)
    // private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnums role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // return Collections.singleton(new SimpleGrantedAuthority("ROLE_" +
    // getRole().name()));
    // }

    // @Override
    // public String getPassword() {
    // return "password"; // ! Removed password field to integrate firebase auth
    // }

    // @Override
    // public String getUsername() {
    // return username;
    // }

}
