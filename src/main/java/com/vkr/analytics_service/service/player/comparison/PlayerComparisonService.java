package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.entity.player.comparisons.PlayerComparison;

public interface PlayerComparisonService {

    void processPlayerComparison1v1(String player1Id, String player2Id, String scope, String scopeId, int seriesOrder);

    void initPlayerComparison(String scope, String scopeId, int seriesOrder);

    PlayerComparison getPlayerComparisonMatch(String scopeId, int seriesOrder);
    PlayerComparison getPlayerComparisonSeries(String scopeId);

    PlayerComparison.PlayerComparison1v1 getPlayerComparison1v1Match(String player1Id, String player2Id, String scopeId, int seriesOrder);
    PlayerComparison.PlayerComparison1v1 getPlayerComparison1v1Series(String player1Id, String player2Id, String scopeId);
}
