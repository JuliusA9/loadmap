package com.loadmap.planner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course code is required.")
    @Size(max = 20, message = "Course code can be at most 20 characters.")
    private String code;

    @NotBlank(message = "Course name is required.")
    @Size(max = 80, message = "Course name can be at most 80 characters.")
    private String name;

    @Min(value = 0, message = "Credits can't be negative.")
    @Max(value = 12, message = "Credits can be at most 12.")
        private int credits;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore  // never send the owner (or their password hash!) to the browser
    private User owner;

    public Course() {}  // Spring needs an empty constructor to build objects from JSON

    public Course(Long id, String code, String name, int credits) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    
}
