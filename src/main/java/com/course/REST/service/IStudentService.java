package com.course.REST.service;

import com.course.REST.model.Student;

import java.util.List;

public interface IStudentService {

    Student findById(Long id);
    List<Student> getAll();

    Student save(Student student);

    Student update(Student student, Long id);

    void delete(Long id);
}
