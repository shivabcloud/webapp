package com.project.healthcheck.Service;

import com.project.healthcheck.Pojo.User;
import com.project.healthcheck.Pojo.UserResponse;

import java.util.Optional;

public interface IUserService {
    public Optional<User> getUserAllDetails(String username);
    public UserResponse createUser(User user);
    public UserResponse updateUser(User user);
    public UserResponse getUser(String username);
    public UserResponse userVerification(String token);
}
