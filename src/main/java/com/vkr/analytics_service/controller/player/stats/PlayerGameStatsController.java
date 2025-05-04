package com.vkr.analytics_service.controller.player.stats;

import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.service.player.game.map.PlayerGameStatsOnMapService;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-stats/players/{playerId}")
public class PlayerGameStatsController {

    private final PlayerGameStatsService statsService;
    private final PlayerGameStatsOnMapService onMapService;

    // --- GLOBAL ---

    @GetMapping("/global")
    public PlayerGameStats getGlobalStats(
            @PathVariable String playerId
    ) {
        return statsService.getGlobalPlayerGameStats(playerId);
    }

    @GetMapping("/global/map/{map}")
    public PlayerGameStatsOnMap getGlobalStatsOnMap(
            @PathVariable String playerId,
            @PathVariable String map
    ) {
        return onMapService.getGlobalPlayerGameStatsOnMap(playerId, map);
    }

    // --- TOURNAMENT ---

    @GetMapping("/tournament/{tournamentId}")
    public PlayerGameStats getTournamentStats(
            @PathVariable String playerId,
            @PathVariable String tournamentId
    ) {
        return statsService.getTournamentPlayerGameStats(playerId, tournamentId);
    }

    @GetMapping("/tournament/{tournamentId}/map/{map}")
    public PlayerGameStatsOnMap getTournamentStatsOnMap(
            @PathVariable String playerId,
            @PathVariable String tournamentId,
            @PathVariable String map
    ) {
        return onMapService.getTournamentPlayerGameStatsOnMap(playerId, tournamentId, map);
    }

    // --- SERIES ---

    @GetMapping("/series/{seriesId}")
    public PlayerGameStats getSeriesStats(
            @PathVariable String playerId,
            @PathVariable String seriesId
    ) {
        return statsService.getSeriesPlayerGameStats(playerId, seriesId);
    }

    @GetMapping("/series/{seriesId}/map/{map}")
    public PlayerGameStatsOnMap getSeriesStatsOnMap(
            @PathVariable String playerId,
            @PathVariable String seriesId,
            @PathVariable String map
    ) {
        return onMapService.getSeriesPlayerGameStatsOnMap(playerId, seriesId, map);
    }

    // --- MATCH ---

    @GetMapping("/match/{matchId}")
    public PlayerGameStats getMatchStats(
            @PathVariable String playerId,
            @PathVariable String matchId
    ) {
        return statsService.getMatchPlayerGameStats(playerId, matchId);
    }

    @GetMapping("/match/{matchId}/map/{map}")
    public PlayerGameStatsOnMap getMatchStatsOnMap(
            @PathVariable String playerId,
            @PathVariable String matchId,
            @PathVariable String map
    ) {
        return onMapService.getMatchPlayerGameStatsOnMap(playerId, matchId, map);
    }
}
