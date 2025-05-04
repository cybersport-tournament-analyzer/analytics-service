package com.vkr.analytics_service.service.player.weapon;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.entity.player.overall.PlayerWeaponStats;
import com.vkr.analytics_service.repository.player.overall.PlayerWeaponStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerWeaponStatsServiceImpl implements PlayerWeaponStatsService {

    private final PlayerWeaponStatsRepository playerWeaponStatsRepository;

    @Override
    public Page<PlayerWeaponStats> getAllWeaponStats(Pageable pageable) {
        return playerWeaponStatsRepository.findAll(pageable);
    }

    @Override
    public void processKillEvents(List<KillEventDto> kills, String scope, String scopeId) {

        Map<String, PlayerWeaponStats> statsCache = new HashMap<>();

        for (KillEventDto kill : kills) {
            String steamId = kill.getKillerSteamId();
            String weapon = kill.getWeapon();

            String id = steamId + "-" + scope + "-" + scopeId;

            PlayerWeaponStats stats = playerWeaponStatsRepository.findById(id)
                    .orElseGet(() -> {
                        PlayerWeaponStats newStats = new PlayerWeaponStats();
                        newStats.setId(id);
                        newStats.setSteamId(steamId);
                        newStats.setWeapon(weapon);
                        newStats.setScope(scope);
                        newStats.setScopeId(scopeId);
                        return newStats;
                    });

            stats.setKills(stats.getKills() + 1);

            if (kill.isHeadshot()) stats.setHeadshots(stats.getHeadshots() + 1);
            if (kill.isPenetrated()) stats.setWallbangs(stats.getWallbangs() + 1);
            if (kill.isNoscope()) stats.setNoscopes(stats.getNoscopes() + 1);
            if (kill.isSmoke()) stats.setThroughSmoke(stats.getThroughSmoke() + 1);
        }
        playerWeaponStatsRepository.saveAll(statsCache.values());

    }

    @Override
    public PlayerWeaponStats getGlobalPlayerWeaponStats(String playerId) {
        return null;
    }

    @Override
    public PlayerWeaponStats getTournamentPlayerWeaponStats(String playerId, String tournamentId) {
        return null;
    }

    @Override
    public PlayerWeaponStats getSeriesPlayerWeaponStats(String playerId, String tournamentMatchId) {
        return null;
    }

    @Override
    public PlayerWeaponStats getMatchPlayerWeaponStats(String playerId, String tournamentMatchId) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

}
