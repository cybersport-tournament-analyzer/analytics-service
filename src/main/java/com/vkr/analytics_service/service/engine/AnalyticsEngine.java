package com.vkr.analytics_service.service.engine;

import com.vkr.analytics_service.dto.matchmaking.Match;

public interface AnalyticsEngine {

    void calculateBasicExtendedStats(String playerGameStatsId, Match match);

    void calculateComplexExtendedStats(String playerGameStatsId);

    void calculateOverallRating(String playerGameStatsId);

    void calculateBestWeapon(String playerGameStatsId);

    void processSideWins(); //ct t wins %

}
