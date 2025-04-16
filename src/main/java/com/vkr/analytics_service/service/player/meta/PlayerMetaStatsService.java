package com.vkr.analytics_service.service.player.meta;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PlayerMetaStatsService {

    Page<PlayerMetaStats> getAllMetaStats(Pageable pageable);

    void updateMetaKillsStats(List<KillEventDto> killEvents, Match match, String scope, String scopeId);

    void updateMetaPlayedStats(Match match, String scope, String scopeId);

    Page<PlayerMetaStats> getGlobalPlayerMetaStats(String playerId, Pageable pageable);
    Page<PlayerMetaStats> getTournamentPlayerMetaStats(String playerId, String tournamentId, Pageable pageable);
    Page<PlayerMetaStats> getSeriesPlayerMetaStats(String playerId, String tournamentMatchId, Pageable pageable);
    Page<PlayerMetaStats> getMatchPlayerMetaStats(String playerId, String tournamentMatchId, Pageable pageable);

    PlayerMetaStats getGlobalPlayerMetaStatsOnMap(String playerId, String map, Pageable pageable);
    PlayerMetaStats getTournamentPlayerMetaStatsOnMap(String playerId, String tournamentId, String map, Pageable pageable);
    PlayerMetaStats getSeriesPlayerMetaStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable);
    PlayerMetaStats getMatchPlayerMetaStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable);

    void deleteAll();
}
