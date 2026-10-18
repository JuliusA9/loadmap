package com.loadmap.planner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required.")
    @Size(max = 80, message = "Title can be at most 80 characters.")
    private String title;            // e.g. "Exam 2"

    @NotNull(message = "Due date is required.")
    private LocalDate dueDate;       // e.g. 2026-10-23

    @Min(value = 0, message = "Points can't be negative.")
    private int points;              // e.g. 100

    @DecimalMin(value = "0.0", message = "Share of grade can't be negative.")
    @DecimalMax(value = "100.0", message = "Share of grade can be at most 100%.")
    private double weightPercent;    // % of final grade, e.g. 10.0

    @NotNull(message = "Type is required.")
    @Enumerated(EnumType.STRING)     // store "EXAM" as text, not a number
    private AssignmentType type;

    @ManyToOne                       // many assignments belong to one course
    @JoinColumn(name = "course_id")  // the column linking to the course table
    private Course course;

    public Assignment() {}

    public Assignment(String title, LocalDate dueDate, int points, double weightPercent, AssignmentType type, Course course) {
        this.title = title;
        this.dueDate = dueDate;
        this.points = points;
        this.weightPercent = weightPercent;
        this.type = type;
        this.course = course;
    }

       public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public double getWeightPercent() { return weightPercent; }
    public void setWeightPercent(double weightPercent) { this.weightPercent = weightPercent; }
    public AssignmentType getType() { return type; }
    public void setType(AssignmentType type) { this.type = type; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}