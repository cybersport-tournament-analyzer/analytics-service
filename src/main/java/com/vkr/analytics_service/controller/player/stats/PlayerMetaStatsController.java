package com.vkr.analytics_service.controller.player.stats;

import com.vkr.analytics_service.entity.player.map.PlayerMetaStatsOnMap;
import com.vkr.analytics_service.entity.player.overall.PlayerMetaStats;
import com.vkr.analytics_service.service.player.meta.map.PlayerMetaStatsOnMapService;
import com.vkr.analytics_service.service.player.meta.overall.PlayerMetaStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meta-stats/players/{playerId}")
public class PlayerMetaStatsController {

    private final PlayerMetaStatsService playerMetaStatsService;
    private final PlayerMetaStatsOnMapService playerMetaStatsOnMapService;

    // --- GLOBAL ---

    @GetMapping("/global")
    public PlayerMetaStats getGlobalStats(
            @PathVariable String playerId
    ) {
        return playerMetaStatsService.getGlobalPlayerMetaStats(playerId);
    }

    @GetMapping("/global/map/{map}")
    public PlayerMetaStatsOnMap getGlobalStatsOnMap(
            @PathVariable String playerId,
            @PathVariable String map
    ) {
        return playerMetaStatsOnMapService.getGlobalPlayerMetaStatsOnMap(playerId, map);
    }

    // --- TOURNAMENT ---

    @GetMapping("/tournament/{tournamentId}")
    public PlayerMetaStats getTournamentStats(
            @PathVariable String playerId,
            @PathVariable String tournamentId
    ) {
        return playerMetaStatsService.getTournamentPlayerMetaStats(playerId, tournamentId);
    }

    @GetMapping("/tournament/{tournamentId}/map/{map}")
    public PlayerMetaStatsOnMap getTournamentStatsOnMap(
            @PathVariable String playerId,
            @PathVariable String tournamentId,
            @PathVariable String map
    ) {
        return playerMetaStatsOnMapService.getTournamentPlayerMetaStatsOnMap(playerId, tournamentId, map);
    }
}
