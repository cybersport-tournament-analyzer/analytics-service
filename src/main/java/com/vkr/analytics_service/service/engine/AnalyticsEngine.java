package com.vkr.analytics_service.service.engine;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.round.RoundStats;

import java.util.List;

public interface AnalyticsEngine {

    void calculateBasicExtendedStats(String playerGameStatsId, Match match);

    boolean isUsefulRound(String playerGameStatsId, RoundStats roundStats, Match match);

    void calculateKast(String playerGameStatsId, int seriesOrder);

    void calculateOverallRating(String playerGameStatsId, List<PlayerStatsRaw> players);

    void calculateBestWeapon(String playerGameStatsId);

}
