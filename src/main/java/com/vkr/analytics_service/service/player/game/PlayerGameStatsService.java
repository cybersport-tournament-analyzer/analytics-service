package com.vkr.analytics_service.service.player.game;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.PlayerGameStats;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerGameStatsService {

    void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players, Match match);

    Page<PlayerGameStats> getAllGameStats(Pageable pageable);

    void deleteAllGameStats();

    Page<PlayerGameStats> getGlobalPlayerGameStats(String playerId, Pageable pageable);
    Page<PlayerGameStats> getTournamentPlayerGameStats(String playerId, String tournamentId, Pageable pageable);
    Page<PlayerGameStats> getSeriesPlayerGameStats(String playerId, String tournamentMatchId, Pageable pageable);
    Page<PlayerGameStats> getMatchPlayerGameStats(String playerId, String tournamentMatchId, Pageable pageable);

    PlayerGameStats getGlobalPlayerGameStatsOnMap(String playerId, String map, Pageable pageable);
    PlayerGameStats getTournamentPlayerGameStatsOnMap(String playerId, String tournamentId, String map, Pageable pageable);
    PlayerGameStats getSeriesPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable);
    PlayerGameStats getMatchPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable);

}
