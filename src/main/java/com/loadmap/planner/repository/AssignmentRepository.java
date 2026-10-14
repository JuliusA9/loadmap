package com.loadmap.planner.repository;

import com.loadmap.planner.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourseIdOrderByDueDate(Long courseId);
    List<Assignment> findByCourseOwnerIdOrderByDueDate(Long ownerId);
    Optional<Assignment> findByIdAndCourseOwnerId(Long id, Long ownerId);
}