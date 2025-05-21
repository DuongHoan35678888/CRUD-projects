package com.bezkoder.spring.datajpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class Users {

    @Id
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "Username must not be blank")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password must not be blank")
    private String password;

    @Column(name = "salt", nullable = false)
    @NotBlank(message = "Salt must not be blank")
    private String salt;

    public Users(String id, String username, String password, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }
}
