package com.loadmap.planner.config;

import com.loadmap.planner.model.Assignment;
import com.loadmap.planner.model.AssignmentType;
import com.loadmap.planner.model.Course;
import com.loadmap.planner.repository.AssignmentRepository;
import com.loadmap.planner.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CourseRepository courseRepo;
    private final AssignmentRepository assignmentRepo;

    public DataSeeder(CourseRepository courseRepo, AssignmentRepository assignmentRepo) {
        this.courseRepo = courseRepo;
        this.assignmentRepo = assignmentRepo;
    }

    @Override
    public void run(String... args) {
        if (courseRepo.count() == 0) {
            courseRepo.save(new Course(null, "IS 147", "Intro to Computer Programming", 3));
            courseRepo.save(new Course(null, "GES 102", "People, Place, and Environment", 3));
            courseRepo.save(new Course(null, "MATH 155", "Math 155", 3));
        }

        if (assignmentRepo.count() == 0) {
            Course is147 = courseRepo.findByCode("IS 147").orElseThrow();
            Course ges102 = courseRepo.findByCode("GES 102").orElseThrow();

          assignmentRepo.save(new Assignment("Quiz 5 & 6", LocalDate.of(2026, 10, 19), 40, 4.4, AssignmentType.QUIZ, is147));
assignmentRepo.save(new Assignment("Exam 2", LocalDate.of(2026, 10, 23), 100, 10, AssignmentType.EXAM, is147));
assignmentRepo.save(new Assignment("Exam 3", LocalDate.of(2026, 12, 4), 100, 10, AssignmentType.EXAM, is147));
assignmentRepo.save(new Assignment("Semester Project", LocalDate.of(2026, 12, 7), 100, 25, AssignmentType.PROJECT, is147));

assignmentRepo.save(new Assignment("Midterm", LocalDate.of(2026, 10, 20), 150, 15, AssignmentType.EXAM, ges102));
assignmentRepo.save(new Assignment("Final Exam", LocalDate.of(2026, 12, 17), 200, 20, AssignmentType.EXAM, ges102));
        }
    }
}
