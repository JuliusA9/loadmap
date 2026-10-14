package com.loadmap.planner.controller;

import com.loadmap.planner.model.Assignment;
import com.loadmap.planner.repository.AssignmentRepository;
import com.loadmap.planner.repository.CourseRepository;
import com.loadmap.planner.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentRepository assignmentRepo;
    private final CourseRepository courseRepo;
    private final CurrentUserService currentUser;

    public AssignmentController(AssignmentRepository assignmentRepo, CourseRepository courseRepo, CurrentUserService currentUser) {
        this.assignmentRepo = assignmentRepo;
        this.courseRepo = courseRepo;
        this.currentUser = currentUser;
    }

    // GET /api/assignments → only MY assignments
    @GetMapping("/assignments")
    public List<Assignment> getAll() {
        return assignmentRepo.findByCourseOwnerIdOrderByDueDate(currentUser.get().getId());
    }

    // GET /api/courses/1/assignments → assignments for one of MY courses
    @GetMapping("/courses/{courseId}/assignments")
    public ResponseEntity<List<Assignment>> getForCourse(@PathVariable Long courseId) {
        return courseRepo.findByIdAndOwnerId(courseId, currentUser.get().getId())
                .map(course -> ResponseEntity.ok(assignmentRepo.findByCourseIdOrderByDueDate(course.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/courses/1/assignments → add to one of MY courses
    @PostMapping("/courses/{courseId}/assignments")
    public ResponseEntity<Assignment> add(@PathVariable Long courseId, @RequestBody Assignment assignment) {
        return courseRepo.findByIdAndOwnerId(courseId, currentUser.get().getId())
                .map(course -> {
                    assignment.setId(null);
                    assignment.setCourse(course);
                    return ResponseEntity.ok(assignmentRepo.save(assignment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/assignments/5 → delete one of MY assignments
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return assignmentRepo.findByIdAndCourseOwnerId(id, currentUser.get().getId())
                .map(a -> {
                    assignmentRepo.delete(a);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}