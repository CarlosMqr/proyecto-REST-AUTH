package com.course.REST.service;

import com.course.REST.model.Role;
import com.course.REST.model.User;
import com.course.REST.repository.IUserRepository;

import com.course.REST.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
   @Transactional
    public User save(User user) {
        Optional<Role> optionalRoleUser = rolRepository.findByName("ROLE_USER");
        List<Role> roles =  new ArrayList<>();

        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()){
            Optional<Role> optionalRoleAdmin = rolRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }



}
