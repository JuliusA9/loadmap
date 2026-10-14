package com.loadmap.planner.controller;

import com.loadmap.planner.model.Course;
import com.loadmap.planner.repository.AssignmentRepository;
import com.loadmap.planner.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository repo;
    private final AssignmentRepository assignmentRepo;

    public CourseController(CourseRepository repo, AssignmentRepository assignmentRepo) {
        this.repo = repo;
        this.assignmentRepo = assignmentRepo;
    }

    // GET /api/courses → every course
    @GetMapping
    public List<Course> getAll() {
        return repo.findAll();
    }

    // GET /api/courses/1 → one course, or 404
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/courses → add a course
    @PostMapping
    public Course add(@RequestBody Course course) {
        course.setId(null);
        return repo.save(course);
    }

    // DELETE /api/courses/1 → remove a course and its assignments
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Assignments point to this course, so they must go first
        assignmentRepo.deleteAll(assignmentRepo.findByCourseIdOrderByDueDate(id));
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}