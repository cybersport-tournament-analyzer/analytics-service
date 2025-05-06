package com.vkr.analytics_service.controller.team;

import com.vkr.analytics_service.entity.team.TeamMetaStats;
import com.vkr.analytics_service.entity.tournament.TournamentMetaStats;
import com.vkr.analytics_service.service.team.TeamStatsService;
import com.vkr.analytics_service.service.tournament.TournamentStatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stats/teams")
@RequiredArgsConstructor
@Tag(name = "Team stats controller")
public class TeamStatsController {

    private final TeamStatsService teamStatsService;

    @GetMapping("/{tournamentId}/{teamName}")
    public List<TeamMetaStats> getTeamStats(@PathVariable String tournamentId, @PathVariable String teamName) {
        return teamStatsService.getAllTeamMetaStats(UUID.fromString(tournamentId), teamName);
    }

    @GetMapping("/{tournamentId}/{teamName}/map/{map}")
    public TeamMetaStats getTeamStatsOnMap(@PathVariable String tournamentId, @PathVariable String teamName, @PathVariable String map) {
        return teamStatsService.getTeamMetaStatsForMap(UUID.fromString(tournamentId), teamName, map);
    }

    @DeleteMapping
    public void deleteTeamStats() {
        teamStatsService.deleteAll();
    }
}
