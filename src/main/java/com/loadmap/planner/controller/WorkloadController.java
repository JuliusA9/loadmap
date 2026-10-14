package com.loadmap.planner.controller;

import com.loadmap.planner.service.WeekLoad;
import com.loadmap.planner.service.WorkloadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workload")
public class WorkloadController {

    private final WorkloadService workloadService;

    public WorkloadController(WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @GetMapping
    public List<WeekLoad> getWorkload() {
        return workloadService.weeklyLoad();
    }
}