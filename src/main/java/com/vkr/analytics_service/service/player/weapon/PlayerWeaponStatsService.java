package com.vkr.analytics_service.service.player.weapon;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.entity.player.overall.PlayerWeaponStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerWeaponStatsService {
    Page<PlayerWeaponStats> getAllWeaponStats(Pageable pageable);

    void processKillEvents(List<KillEventDto> killEvents, String scope, String scopeId, int seriesOrder);

    List<PlayerWeaponStats> getGlobalPlayerWeaponStats(String playerId);
    List<PlayerWeaponStats> getTournamentPlayerWeaponStats(String playerId, String tournamentId);
    List<PlayerWeaponStats> getSeriesPlayerWeaponStats(String playerId, String tournamentMatchId);
    List<PlayerWeaponStats> getMatchPlayerWeaponStats(String playerId, String tournamentMatchId, int seriesOrder);

    void deleteAll();
}
