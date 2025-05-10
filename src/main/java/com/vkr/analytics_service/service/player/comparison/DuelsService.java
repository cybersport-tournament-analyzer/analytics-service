package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.entity.player.comparisons.Duels;
import com.vkr.analytics_service.entity.player.comparisons.PlayerDuels;
import com.vkr.analytics_service.entity.round.RoundStats;

import java.util.List;

public interface DuelsService {

    void processDuels(String player1Id, String player2Id, String scope, String scopeId, List<RoundStats> allRounds, int seriesOrder);

    void createDuel(String scope, String scopeId, int seriesOrder);

    PlayerDuels findByPlayers(String player1Id, String player2Id, String scope, String scopeId, int seriesOrder);

    Duels getMatchDuels(String seriesId, int seriesOrder);

    Duels getSeriesDuels(String seriesId);
}
