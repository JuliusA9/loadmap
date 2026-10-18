package com.loadmap.planner.controller;

import com.loadmap.planner.model.Course;
import com.loadmap.planner.repository.AssignmentRepository;
import com.loadmap.planner.repository.CourseRepository;
import com.loadmap.planner.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository repo;
    private final AssignmentRepository assignmentRepo;
    private final CurrentUserService currentUser;

    public CourseController(CourseRepository repo, AssignmentRepository assignmentRepo, CurrentUserService currentUser) {
        this.repo = repo;
        this.assignmentRepo = assignmentRepo;
        this.currentUser = currentUser;
    }

    // GET /api/courses → only MY courses
    @GetMapping
    public List<Course> getAll() {
        return repo.findByOwnerIdOrderByCode(currentUser.get().getId());
    }

    // GET /api/courses/1 → one of MY courses, or 404
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable Long id) {
        return repo.findByIdAndOwnerId(id, currentUser.get().getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/courses → add a course owned by me (validated)
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Course course) {
        Long ownerId = currentUser.get().getId();
        String code = course.getCode().trim().toUpperCase();

        if (repo.existsByOwnerIdAndCodeIgnoreCase(ownerId, code)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "You already have a course called " + code + "."));
        }

        course.setId(null);
        course.setCode(code);
        course.setName(course.getName().trim());
        course.setOwner(currentUser.get());
        return ResponseEntity.ok(repo.save(course));
    }

    // DELETE /api/courses/1 → delete one of MY courses and its assignments
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findByIdAndOwnerId(id, currentUser.get().getId())
                .map(course -> {
                    assignmentRepo.deleteAll(assignmentRepo.findByCourseIdOrderByDueDate(course.getId()));
                    repo.delete(course);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}