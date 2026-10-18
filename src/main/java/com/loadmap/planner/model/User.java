package com.loadmap.planner.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "app_users")  // "user" is a reserved word in SQL, so we pick another table name
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;  // never the real password

    private String termName;          // e.g. "Fall 2026"
    private LocalDate semesterStart;  // first day of classes
    private LocalDate semesterEnd;    // last day of finals

    public User() {}

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }

    public String getTermName() { return termName; }
    public void setTermName(String termName) { this.termName = termName; }
    public LocalDate getSemesterStart() { return semesterStart; }
    public void setSemesterStart(LocalDate semesterStart) { this.semesterStart = semesterStart; }
    public LocalDate getSemesterEnd() { return semesterEnd; }
    public void setSemesterEnd(LocalDate semesterEnd) { this.semesterEnd = semesterEnd; }
}