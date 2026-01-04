package com.course.REST.repository;

import com.course.REST.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISchoolRepository extends JpaRepository<School, Long> {
}
