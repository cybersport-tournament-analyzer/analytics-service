package com.vkr.analytics_service.service.engine.handler;

import com.vkr.analytics_service.kafka.event.matchEnd.MatchEndEvent;
import com.vkr.analytics_service.service.player.meta.overall.PlayerMetaStatsService;
import com.vkr.analytics_service.service.team.TeamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchEndHandler {

    private final PlayerMetaStatsService playerMetaStatsService;
    private final TeamStatsService teamStatsService;

    public void handleMatchEnd(MatchEndEvent event) {

        System.out.println("заполняю мета стату для игроков");

        playerMetaStatsService.updateMetaPlayedStats(event.getMatch(), "tournament", String.valueOf(event.getTournamentId()));
        playerMetaStatsService.updateMetaPlayedStats(event.getMatch(), "global", "global");

        System.out.println("заполняю тим мета стату для игроков " + event.getMatch().getTeam1().getName());
        teamStatsService.aggregateTeamMetaStats(event.getMatch(), event.getTournamentId(), event.getMatch().getTeam1().getName());
        System.out.println("заполняю тим мета стату для игроков " + event.getMatch().getTeam2().getName());
        teamStatsService.aggregateTeamMetaStats(event.getMatch(), event.getTournamentId(), event.getMatch().getTeam2().getName());
    }
}
