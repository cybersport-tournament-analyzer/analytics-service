package com.vkr.analytics_service.service.player.game;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.PlayerGameStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerGameStatsService {

    void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players, Match match);

    Page<PlayerGameStats> getAllGameStats(Pageable pageable);

    void deleteAllGameStats();
}
