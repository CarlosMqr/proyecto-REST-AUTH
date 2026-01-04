package com.course.REST.service;

import com.course.REST.model.School;

import java.util.List;

public interface ISchoolService {
School findById(Long id);
List<School> getAll();

School save(School school);

School update(School school, Long id);

void delete(Long id);
}
