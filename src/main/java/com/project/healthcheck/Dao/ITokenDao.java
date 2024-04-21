package com.project.healthcheck.Dao;

import com.project.healthcheck.Pojo.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenDao extends JpaRepository<VerifyToken,String> {
    Optional<VerifyToken> findByToken(String token);
}
