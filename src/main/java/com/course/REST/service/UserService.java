package com.course.REST.service;

import com.course.REST.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User save(User user);
}
