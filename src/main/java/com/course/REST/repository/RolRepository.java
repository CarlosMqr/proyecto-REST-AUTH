package com.course.REST.repository;

import com.course.REST.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
