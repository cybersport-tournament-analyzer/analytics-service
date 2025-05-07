package com.vkr.analytics_service.controller.player.stats;

import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.service.round.RoundStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rounds")
public class RoundStatsController {

    private final RoundStatsService roundStatsService;

    @GetMapping
    public Page<RoundStats> getStats(Pageable pageable) {
        return roundStatsService.getAll(pageable);
    }
}
