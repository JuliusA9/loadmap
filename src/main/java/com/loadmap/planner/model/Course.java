package com.loadmap.planner.model;

public class Course {
    private Long id;
    private String code;     // e.g. "IS 147"
    private String name;     // e.g. "Intro to Computer Programming"
    private int credits;

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
    
}
