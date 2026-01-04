package com.course.REST.service;
import com.course.REST.model.Student;
import com.course.REST.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student update(Student student, Long id) {
    Optional <Student> student1 = studentRepository.findById(id);

    if (student1.isPresent()){
        Student existingStudent = student1.get();
        existingStudent.setName(student.getName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setRegistered(student.isRegistered());

        return studentRepository.save(existingStudent);
    }
    return new Student();
    }


    @Override
    @Transactional
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
