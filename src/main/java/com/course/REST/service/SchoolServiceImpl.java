package com.course.REST.service;

import com.course.REST.model.School;
import com.course.REST.repository.ISchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class SchoolServiceImpl implements ISchoolService {
    @Autowired
    private ISchoolRepository schoolRepository;

    @Override
    @Transactional(readOnly = true)
    public School findById(Long id) {
        return schoolRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<School> getAll() {
        return schoolRepository.findAll();
    }

    @Override
    @Transactional
    public School save(School school) {
        return schoolRepository.save(school);
    }

    @Override
    @Transactional
    public School update(School school, Long id) {
        Optional<School> school1 = schoolRepository.findById(id);
        if (school1.isPresent()) {
            School existingSchool = school1.get();
            existingSchool.setName(school.getName());
            existingSchool.setState(school.getState());
             return schoolRepository.save(existingSchool);
        }
        return new School();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        schoolRepository.deleteById(id);
    }
}
