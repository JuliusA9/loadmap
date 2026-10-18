package com.loadmap.planner.controller;

import com.loadmap.planner.model.User;
import com.loadmap.planner.repository.UserRepository;
import com.loadmap.planner.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final UserRepository users;
    private final CurrentUserService currentUser;

    public SettingsController(UserRepository users, CurrentUserService currentUser) {
        this.users = users;
        this.currentUser = currentUser;
    }

    // The JSON shape for both reading and saving
    public record Semester(String termName, LocalDate semesterStart, LocalDate semesterEnd) {}

    // GET /api/settings → my term (fields are null until I set them)
    @GetMapping
    public Semester get() {
        User u = currentUser.get();
        return new Semester(u.getTermName(), u.getSemesterStart(), u.getSemesterEnd());
    }

    // PUT /api/settings → save my term, after checking it makes sense
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Semester s) {
        String name = s.termName() == null ? "" : s.termName().trim();

        if (name.isEmpty() || name.length() > 40) {
            return error("Give the term a name of up to 40 characters.");
        }
        if (s.semesterStart() == null || s.semesterEnd() == null) {
            return error("Choose a start date and an end date.");
        }
        if (!s.semesterEnd().isAfter(s.semesterStart())) {
            return error("End date must be after the start date.");
        }
        if (ChronoUnit.DAYS.between(s.semesterStart(), s.semesterEnd()) > 366) {
            return error("A term can be at most a year long.");
        }

        User u = currentUser.get();
        u.setTermName(name);
        u.setSemesterStart(s.semesterStart());
        u.setSemesterEnd(s.semesterEnd());
        users.save(u);
        return ResponseEntity.ok(new Semester(name, s.semesterStart(), s.semesterEnd()));
    }

    private ResponseEntity<Map<String, String>> error(String message) {
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}