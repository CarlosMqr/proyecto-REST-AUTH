package com.course.REST.controller;

import com.course.REST.model.User;
import com.course.REST.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> lis(){
        return userService.getAllUsers();
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result){
        if (result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> response = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            response.put(err.getField(), "El campo '" + err.getField() + "' " + err.getDefaultMessage());
        });
        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}
