package com.vkr.analytics_service.controller.player.comparisons;

import com.vkr.analytics_service.entity.player.comparisons.Duels;
import com.vkr.analytics_service.service.player.comparison.DuelsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/duels")
public class DuelsController {

    private final DuelsService duelsService;

    @GetMapping("/{seriesId}")
    public Duels getSeriesDuels(@PathVariable("seriesId") String seriesId) {
        return duelsService.getSeriesDuels(seriesId);
    }

    @GetMapping("/{seriesId}/{seriesOrder}")
    public Duels getSeriesDuels(@PathVariable("seriesId") String seriesId, @PathVariable("seriesOrder") int seriesOrder) {
        return duelsService.getMatchDuels(seriesId, seriesOrder);
    }
}
