package com.vkr.analytics_service.service.player.meta.overall;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.overall.PlayerMetaStats;
import com.vkr.analytics_service.repository.player.overall.PlayerMetaStatsRepository;
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

        Map<String, PlayerMetaStats> statsCache = new HashMap<>();

        for (KillEventDto event : killEvents) {
            String killerId = event.getKillerSteamId();

            String statId = killerId + "-" + scope + "-" + scopeId;

            PlayerMetaStats stats = statsCache.computeIfAbsent(statId, id ->
                    playerMetaStatsRepository.findById(id).orElseGet(() -> PlayerMetaStats.builder()
                            .id(id)
                            .steamId(killerId)
                            .scope(scope)
                            .scopeId(scopeId)
                            .build())
            );
        }

        playerMetaStatsRepository.saveAll(statsCache.values());
    }

    @Override
    public void updateMetaPlayedStats(Match match, String scope, String scopeId) {
        Map<String, PlayerMetaStats> statsCache = new HashMap<>();
        for (Match.Player player : match.getPlayers()) {
            String playerId = player.getSteam_id_64();
            String team = player.getTeam();

            String statId = playerId + "-" + scope + "-" + scopeId;

            PlayerMetaStats stats = statsCache.computeIfAbsent(statId, id ->
                    playerMetaStatsRepository.findById(id).orElseGet(() -> PlayerMetaStats.builder()
                            .id(id)
                            .steamId(playerId)
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
    public PlayerMetaStats getGlobalPlayerMetaStats(String playerId) {

        return null;
    }

    @Override
    public PlayerMetaStats getTournamentPlayerMetaStats(String playerId, String tournamentId) {
        return null;
    }


    @Override
    public void deleteAll() {

    }
}
