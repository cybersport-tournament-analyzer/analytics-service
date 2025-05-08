package com.vkr.analytics_service.service.engine.handler;


import com.vkr.analytics_service.kafka.event.pickbans.PickBansEvent;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import com.vkr.analytics_service.service.team.TeamStatsService;
import com.vkr.analytics_service.service.tournament.TournamentStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PickBansHandler {

    private final TeamStatsService teamStatsService;
    private final TournamentStatsService tournamentStatsService;

    public void handlePickBans(PickBansEvent event) {

        System.out.println("заполняю турнирную стату для " + event.getTournamentId());
        tournamentStatsService.aggregateTournamentStats(event.getPickbans(), event.getTournamentId());

        System.out.println("заполняю тим стату для " + event.getTeam1Name());
        teamStatsService.aggregateTeamPickBanStats(event.getPickbans(), event.getTournamentId(), event.getTeam1Name(), "team1");
        System.out.println("заполняю тим стату для " + event.getTeam2Name());
        teamStatsService.aggregateTeamPickBanStats(event.getPickbans(), event.getTournamentId(), event.getTeam2Name(), "team2");
    }
}
