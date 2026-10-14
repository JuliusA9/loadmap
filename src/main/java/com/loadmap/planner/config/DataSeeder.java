package com.loadmap.planner.config;

import com.loadmap.planner.model.Course;
import com.loadmap.planner.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CourseRepository repo;

    public DataSeeder(CourseRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {  // only seed an empty database
            repo.save(new Course(null, "IS 147", "Intro to Computer Programming", 3));
            repo.save(new Course(null, "GES 102", "People, Place, and Environment", 3));
            repo.save(new Course(null, "MATH 155", "Math 155", 3));
        }
    }
}