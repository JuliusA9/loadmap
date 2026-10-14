package com.loadmap.planner.repository;

import com.loadmap.planner.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}