package com.vkr.analytics_service.controllers;

import com.vkr.analytics_service.entity.Match;
import com.vkr.analytics_service.entity.MatchSeries;
import com.vkr.analytics_service.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Контроллер остается без изменений
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;


    @GetMapping
    public ResponseEntity<List<MatchSeries>> getAllSeries() {
        return ResponseEntity.ok(matchService.getAllSeries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchSeries> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getSeriesById(id));
    }
}
