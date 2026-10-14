package com.loadmap.planner.controller;

import com.loadmap.planner.model.Course;
import com.loadmap.planner.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository repo;

    // Spring passes in the repository automatically ("dependency injection")
    public CourseController(CourseRepository repo) {
        this.repo = repo;
    }

    // GET /api/courses → every course in the database
    @GetMapping
    public List<Course> getAll() {
        return repo.findAll();
    }

    // GET /api/courses/1 → one course, or a 404 error if it doesn't exist
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/courses → save a new course
    @PostMapping
    public Course add(@RequestBody Course course) {
        course.setId(null);  // let the database choose the id
        return repo.save(course);
    }
}