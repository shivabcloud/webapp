package com.project.healthcheck.Dao;

import com.project.healthcheck.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface IUserDao extends JpaRepository<User,String>{
    Optional<User> findByUsername(String username);
    
}
