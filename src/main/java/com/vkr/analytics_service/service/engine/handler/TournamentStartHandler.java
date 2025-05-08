package com.vkr.analytics_service.service.engine.handler;

import com.vkr.analytics_service.kafka.event.tournamentStart.TournamentStartEvent;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TournamentStartHandler {

    private final PlayerGameStatsService playerGameStatsService;

    public void initStats(TournamentStartEvent event) {
        event.getPlayerIds().forEach(playerId -> {
            System.out.println("инициирую турнирную гейм стату для " + playerId);
            playerGameStatsService.initGlobalStats(playerId, String.valueOf(event.getTournamentId()));
        });
    }
}
