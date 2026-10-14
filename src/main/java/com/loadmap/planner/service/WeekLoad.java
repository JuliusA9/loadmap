package com.loadmap.planner.service;

import java.time.LocalDate;
import java.util.List;

public record WeekLoad(LocalDate weekOf, int score, String level, List<String> items) {}
