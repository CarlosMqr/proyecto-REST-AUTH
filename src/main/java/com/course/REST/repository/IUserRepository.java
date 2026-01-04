package com.course.REST.repository;

import com.course.REST.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepository  extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String name);
}
