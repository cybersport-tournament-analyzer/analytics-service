package com.vkr.analytics_service.service.player.meta;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import com.vkr.analytics_service.repository.player.PlayerMetaStatsRepository;
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
public class PlayerMetaStatsServiceImpl implements PlayerMetaStatsService {

    private final PlayerMetaStatsRepository playerMetaStatsRepository;


    @Override
    public Page<PlayerMetaStats> getAllMetaStats(Pageable pageable) {
        return playerMetaStatsRepository.findAll(pageable);
    }

    @Override
    public void updateMetaKillsStats(List<KillEventDto> killEvents, Match match, String scope, String scopeId) {
        String map = match.getSettings().getMap();

        Map<String, PlayerMetaStats> statsCache = new HashMap<>();

        for (KillEventDto event : killEvents) {
            String killerId = event.getKillerSteamId();

            String statId = killerId + "-" + map + "-" + scope + "-" + scopeId;

            PlayerMetaStats stats = statsCache.computeIfAbsent(statId, id ->
                    playerMetaStatsRepository.findById(id).orElseGet(() -> PlayerMetaStats.builder()
                            .id(id)
                            .steamId(killerId)
                            .map(map)
                            .scope(scope)
                            .scopeId(scopeId)
                            .build())
            );

            stats.setTotalKills(stats.getTotalKills() + 1);
            if (event.isHeadshot()) stats.setHeadshots(stats.getHeadshots() + 1);
            if (event.isPenetrated()) stats.setWallbangs(stats.getWallbangs() + 1);
            if (event.isNoscope()) stats.setNoscopes(stats.getNoscopes() + 1);
            if (event.isSmoke()) stats.setSmokeKills(stats.getSmokeKills() + 1);
        }

        playerMetaStatsRepository.saveAll(statsCache.values());
    }

    @Override
    public void updateMetaPlayedStats(Match match, String scope, String scopeId) {
        String map = match.getSettings().getMap();
        Map<String, PlayerMetaStats> statsCache = new HashMap<>();
        for (Match.Player player : match.getPlayers()) {
            String playerId = player.getSteam_id_64();
            String team = player.getTeam();

            String statId = playerId + "-" + map + "-" + scope + "-" + scopeId;

            PlayerMetaStats stats = statsCache.computeIfAbsent(statId, id ->
                    playerMetaStatsRepository.findById(id).orElseGet(() -> PlayerMetaStats.builder()
                            .id(id)
                            .steamId(playerId)
                            .map(map)
                            .scope(scope)
                            .scopeId(scopeId)
                            .build())
            );

            stats.setPlayed(stats.getPlayed() + 1);
            if (team.equals(match.getTeam1().getName())) {
                if (match.getTeam1().getStats().getScore() > match.getTeam2().getStats().getScore()) {
                    stats.setWon(stats.getWon() + 1);
                } else stats.setLost(stats.getLost() + 1);
            } else {
                if (match.getTeam2().getStats().getScore() > match.getTeam1().getStats().getScore()) {
                    stats.setWon(stats.getWon() + 1);
                } else stats.setLost(stats.getLost() + 1);
            }
            stats.setWinRate((double) stats.getWon() / stats.getPlayed());
        }

        playerMetaStatsRepository.saveAll(statsCache.values());
    }

    @Override
    public Page<PlayerMetaStats> getGlobalPlayerMetaStats(String playerId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PlayerMetaStats> getTournamentPlayerMetaStats(String playerId, String tournamentId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PlayerMetaStats> getSeriesPlayerMetaStats(String playerId, String tournamentMatchId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PlayerMetaStats> getMatchPlayerMetaStats(String playerId, String tournamentMatchId, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerMetaStats getGlobalPlayerMetaStatsOnMap(String playerId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerMetaStats getTournamentPlayerMetaStatsOnMap(String playerId, String tournamentId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerMetaStats getSeriesPlayerMetaStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerMetaStats getMatchPlayerMetaStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
