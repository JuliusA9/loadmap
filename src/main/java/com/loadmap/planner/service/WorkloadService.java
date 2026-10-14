package com.loadmap.planner.service;

import com.loadmap.planner.model.Assignment;
import com.loadmap.planner.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class WorkloadService {

    private final AssignmentRepository assignmentRepo;

    public WorkloadService(AssignmentRepository assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }

    // Rate one assignment from 1 to 5
    public int impactOf(Assignment a) {
        int rating = switch (a.getType()) {
            case EXAM, PROJECT -> 4;
            case QUIZ -> 2;
            case HOMEWORK, DISCUSSION -> 1;
        };
        if (a.getWeightPercent() >= 10) {
            rating++;  // big share of the grade = higher risk
        }
        return Math.min(rating, 5);
    }

    // Group assignments by week and score each week
    public List<WeekLoad> weeklyLoad() {
        // TreeMap keeps the weeks sorted by date
        Map<LocalDate, List<Assignment>> byWeek = new TreeMap<>();

        for (Assignment a : assignmentRepo.findAll()) {
            LocalDate monday = a.getDueDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            byWeek.computeIfAbsent(monday, k -> new ArrayList<>()).add(a);
        }

        List<WeekLoad> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Assignment>> week : byWeek.entrySet()) {
            int score = 0;
            List<String> items = new ArrayList<>();
            for (Assignment a : week.getValue()) {
                int impact = impactOf(a);
                score += impact;
                items.add(a.getCourse().getCode() + " " + a.getTitle() + " (" + impact + "/5)");
            }
            result.add(new WeekLoad(week.getKey(), score, levelFor(score), items));
        }
        return result;
    }

    private String levelFor(int score) {
        if (score >= 8) return "HEAVY";
        if (score >= 4) return "MODERATE";
        return "LIGHT";
    }
}