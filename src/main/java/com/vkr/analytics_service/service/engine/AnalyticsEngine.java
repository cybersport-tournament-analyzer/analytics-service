package com.vkr.analytics_service.service.engine;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.round.RoundStats;

public interface AnalyticsEngine {

    void calculateBasicExtendedStats(String playerGameStatsId, Match match);

    boolean isUsefulRound(String playerGameStatsId, RoundStats roundStats, Match match);

    void calculateKast(String playerGameStatsId, int seriesOrder);

    void calculateOverallRating(String playerGameStatsId);

    void calculateBestWeapon(String playerGameStatsId);

//    void calculateBasicExtendedStatsOnMap(String playerGameOnMapStatsId, Match match);
//
//    void calculateOverallRatingOnMap(String playerGameOnMapStatsId);
//
//    void calculateBestWeaponOnMap(String playerGameOnMapStatsId);


}
