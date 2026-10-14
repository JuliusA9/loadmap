package com.loadmap.planner.controller;

import com.loadmap.planner.model.Course;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    // Temporary storage in memory. We'll swap this for a real database next.
    private final List<Course> courses = new ArrayList<>(List.of(
        new Course(1L, "IS 147", "Intro to Computer Programming", 3),
        new Course(2L, "GES 102", "People, Place, and Environment", 3),
        new Course(3L, "MATH 155", "Math 155", 3)
    ));
    private long nextId = 4;

    // GET /api/courses → returns every course
    @GetMapping
    public List<Course> getAll() {
        return courses;
    }

    // GET /api/courses/1 → returns the course with id 1
    @GetMapping("/{id}")
    public Course getOne(@PathVariable Long id) {
        return courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // POST /api/courses → adds a new course
    @PostMapping
    public Course add(@RequestBody Course course) {
        course.setId(nextId++);
        courses.add(course);
        return course;
    }
}
