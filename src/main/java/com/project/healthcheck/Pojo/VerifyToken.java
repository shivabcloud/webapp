package com.project.healthcheck.Pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class VerifyToken {
    @Id
    private String token;
    @Column
    private String username;
    @Column
    private LocalDateTime expiration;

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }
}
