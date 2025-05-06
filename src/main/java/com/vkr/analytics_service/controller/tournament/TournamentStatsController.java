package com.vkr.analytics_service.controller.tournament;

import com.vkr.analytics_service.entity.tournament.TournamentMetaStats;
import com.vkr.analytics_service.service.tournament.TournamentStatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stats/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournament Stats Controller")
public class TournamentStatsController {

    private final TournamentStatsService tournamentStatsService;

    @GetMapping("/{tournamentId}")
    public List<TournamentMetaStats> getTournamentStats(@PathVariable String tournamentId) {
        return tournamentStatsService.getAllTournamentStats(UUID.fromString(tournamentId));
    }

    @GetMapping("/{tournamentId}/map/{map}")
    public TournamentMetaStats getTournamentStatsOnMap(@PathVariable String tournamentId, @PathVariable String map) {
        return tournamentStatsService.getTournamentStatsForMap(UUID.fromString(tournamentId), map);
    }

    @DeleteMapping
    public void deleteTournamentStats() {
        tournamentStatsService.deleteAll();
    }
}
