package com.loadmap.planner.repository;

import com.loadmap.planner.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByOwnerIdOrderByCode(Long ownerId);
    Optional<Course> findByIdAndOwnerId(Long id, Long ownerId);
}