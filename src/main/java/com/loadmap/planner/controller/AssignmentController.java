package com.loadmap.planner.controller;

import com.loadmap.planner.model.Assignment;
import com.loadmap.planner.model.Course;
import com.loadmap.planner.repository.AssignmentRepository;
import com.loadmap.planner.repository.CourseRepository;
import com.loadmap.planner.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    // POST /api/courses/1/assignments → add to one of MY courses (validated)
    @PostMapping("/courses/{courseId}/assignments")
    public ResponseEntity<Assignment> add(@PathVariable Long courseId, @Valid @RequestBody Assignment assignment) {
        return courseRepo.findByIdAndOwnerId(courseId, currentUser.get().getId())
                .map(course -> {
                    assignment.setId(null);
                    assignment.setCompleted(false);
                    assignment.setTitle(assignment.getTitle().trim());
                    assignment.setCourse(course);
                    return ResponseEntity.ok(assignmentRepo.save(assignment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/assignments/5 → edit one of MY assignments.
    // Send "course": {"id": 2} to move it to another of MY courses; leave it out to keep the course.
    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> update(@PathVariable Long id, @Valid @RequestBody Assignment changes) {
        Long ownerId = currentUser.get().getId();
        Optional<Assignment> found = assignmentRepo.findByIdAndCourseOwnerId(id, ownerId);
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Assignment a = found.get();

        if (changes.getCourse() != null && changes.getCourse().getId() != null) {
            Optional<Course> course = courseRepo.findByIdAndOwnerId(changes.getCourse().getId(), ownerId);
            if (course.isEmpty()) {
                return ResponseEntity.notFound().build();  // not one of my courses
            }
            a.setCourse(course.get());
        }

        a.setTitle(changes.getTitle().trim());
        a.setDueDate(changes.getDueDate());
        a.setPoints(changes.getPoints());
        a.setWeightPercent(changes.getWeightPercent());
        a.setType(changes.getType());
        // "completed" is left alone here; the PATCH below is the way to change it
        return ResponseEntity.ok(assignmentRepo.save(a));
    }

    // The JSON for the PATCH below: {"completed": true}
    public record CompletedChange(boolean completed) {}

    // PATCH /api/assignments/5/completed → tick one of MY assignments done (or not done)
    @PatchMapping("/assignments/{id}/completed")
    public ResponseEntity<Assignment> setCompleted(@PathVariable Long id, @RequestBody CompletedChange change) {
        return assignmentRepo.findByIdAndCourseOwnerId(id, currentUser.get().getId())
                .map(a -> {
                    a.setCompleted(change.completed());
                    return ResponseEntity.ok(assignmentRepo.save(a));
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