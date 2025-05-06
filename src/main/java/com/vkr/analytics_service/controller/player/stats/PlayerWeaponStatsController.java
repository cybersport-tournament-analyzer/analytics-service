package com.vkr.analytics_service.controller.player.stats;


import com.vkr.analytics_service.entity.player.overall.PlayerWeaponStats;
import com.vkr.analytics_service.service.player.weapon.PlayerWeaponStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weapons/players/{playerId}")
public class PlayerWeaponStatsController {

    private final PlayerWeaponStatsService playerWeaponStatsService;

    @GetMapping("/global")
    public List<PlayerWeaponStats> getPlayerGlobalWeaponStats(@PathVariable String playerId) {
        return playerWeaponStatsService.getGlobalPlayerWeaponStats(playerId);
    }

    @GetMapping("/tournament/{tournamentId}")
    public List<PlayerWeaponStats> getPlayerTournamentWeaponStats(@PathVariable String playerId, @PathVariable String tournamentId) {
        return playerWeaponStatsService.getTournamentPlayerWeaponStats(playerId, tournamentId);
    }

    @GetMapping("/series/{seriesId}")
    public List<PlayerWeaponStats> getPlayerSeriesWeaponStats(@PathVariable String playerId, @PathVariable String seriesId) {
        return playerWeaponStatsService.getSeriesPlayerWeaponStats(playerId, seriesId);
    }

    @GetMapping("/series/{seriesId}/{seriesOrder}")
    public List<PlayerWeaponStats> getPlayerSeriesWeaponStats(@PathVariable String playerId, @PathVariable String seriesId, @PathVariable int seriesOrder) {
        return playerWeaponStatsService.getMatchPlayerWeaponStats(playerId, seriesId, seriesOrder);
    }
}
