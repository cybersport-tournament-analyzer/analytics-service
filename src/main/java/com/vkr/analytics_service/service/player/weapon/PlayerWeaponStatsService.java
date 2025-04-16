package com.vkr.analytics_service.service.player.weapon;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.PlayerWeaponStats;
import com.vkr.analytics_service.entity.team.TeamMetaStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PlayerWeaponStatsService {
    Page<PlayerWeaponStats> getAllWeaponStats(Pageable pageable);

    void processKillEvents(List<KillEventDto> killEvents, String map, String scope, String scopeId);

    Page<PlayerWeaponStats> getGlobalPlayerWeaponStats(String playerId, Pageable pageable);
    Page<PlayerWeaponStats> getTournamentPlayerWeaponStats(String playerId, String tournamentId, Pageable pageable);
    Page<PlayerWeaponStats> getSeriesPlayerWeaponStats(String playerId, String tournamentMatchId, Pageable pageable);
    Page<PlayerWeaponStats> getMatchPlayerWeaponStats(String playerId, String tournamentMatchId, Pageable pageable);

    PlayerWeaponStats getGlobalPlayerWeaponStatsOnMap(String playerId, String map, Pageable pageable);
    PlayerWeaponStats getTournamentPlayerWeaponStatsOnMap(String playerId, String tournamentId, String map, Pageable pageable);
    PlayerWeaponStats getSeriesPlayerWeaponStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable);
    PlayerWeaponStats getMatchPlayerWeaponStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable);

    void deleteAll();
}
