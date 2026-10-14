package com.loadmap.planner.controller;

import com.loadmap.planner.model.Assignment;
import com.loadmap.planner.repository.AssignmentRepository;
import com.loadmap.planner.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentRepository assignmentRepo;
    private final CourseRepository courseRepo;

    public AssignmentController(AssignmentRepository assignmentRepo, CourseRepository courseRepo) {
        this.assignmentRepo = assignmentRepo;
        this.courseRepo = courseRepo;
    }

    // GET /api/assignments → every assignment
    @GetMapping("/assignments")
    public List<Assignment> getAll() {
        return assignmentRepo.findAll();
    }

    // GET /api/courses/1/assignments → assignments for one course, sorted by date
    @GetMapping("/courses/{courseId}/assignments")
    public List<Assignment> getForCourse(@PathVariable Long courseId) {
        return assignmentRepo.findByCourseIdOrderByDueDate(courseId);
    }

    // POST /api/courses/1/assignments → add an assignment to a course
    @PostMapping("/courses/{courseId}/assignments")
    public ResponseEntity<Assignment> add(@PathVariable Long courseId, @RequestBody Assignment assignment) {
        return courseRepo.findById(courseId)
                .map(course -> {
                    assignment.setId(null);
                    assignment.setCourse(course);
                    return ResponseEntity.ok(assignmentRepo.save(assignment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/assignments/5 → remove an assignment
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!assignmentRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assignmentRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}