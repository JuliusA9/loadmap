package com.loadmap.planner.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;     // e.g. "IS 147"
    private String name;     // e.g. "Intro to Computer Programming"
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
