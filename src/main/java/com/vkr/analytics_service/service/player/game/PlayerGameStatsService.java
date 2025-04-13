package com.vkr.analytics_service.service.player.game;

import com.vkr.analytics_service.dto.player.PlayerStatsRaw;

import java.util.List;

public interface PlayerGameStatsService {

    void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players);
}
