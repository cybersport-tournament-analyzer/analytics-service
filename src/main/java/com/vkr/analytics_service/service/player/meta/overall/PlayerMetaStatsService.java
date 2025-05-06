package com.vkr.analytics_service.service.player.meta.overall;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.overall.PlayerMetaStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PlayerMetaStatsService {

    Page<PlayerMetaStats> getAllMetaStats(Pageable pageable);

    void updateMetaPlayedStats(Match match, String scope, String scopeId);

    PlayerMetaStats getGlobalPlayerMetaStats(String playerId);
    PlayerMetaStats getTournamentPlayerMetaStats(String playerId, String tournamentId);

    void deleteAll();
}
