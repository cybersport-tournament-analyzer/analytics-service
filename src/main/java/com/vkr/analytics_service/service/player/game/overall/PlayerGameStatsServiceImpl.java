package com.vkr.analytics_service.service.player.game.overall;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.repository.player.overall.PlayerGameStatsRepository;
import com.vkr.analytics_service.service.engine.AnalyticsEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerGameStatsServiceImpl implements PlayerGameStatsService {

    private final PlayerGameStatsRepository playerGameStatsRepository;

    @Override
    public void aggregate(String scope, String scopeId, List<PlayerStatsRaw> players, Match match, int seriesOrder) {
        for (PlayerStatsRaw raw : players) {
            String id = raw.getSteamId() + "-" + scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X");

            PlayerGameStats stats = playerGameStatsRepository.findById(id).orElse(null);

            if(scope.equals("match")) {
                assert stats != null;
                stats.setKills(raw.getKills());
                stats.setDeaths(raw.getDeaths());
                stats.setAssists(raw.getAssists());

                stats.set_2ks(raw.get_2ks());
                stats.set_3ks(raw.get_3ks());
                stats.set_4ks(raw.get_4ks());
                stats.set_5ks(raw.get_5ks());

                stats.setBlindk(raw.getBlindk());
                stats.setBombk(raw.getBombk());
                stats.setFiredmg(raw.getFiredmg());
                stats.setUniquek(raw.getUniquek());
                stats.setDinks(raw.getDinks());
                stats.setChickenk(raw.getChickenk());

                stats.setKillsWithHeadshot(raw.getKillsWithHeadshot());
                stats.setKillsWithPistol(raw.getKillsWithPistol());
                stats.setKillsWithSniper(raw.getKillsWithSniper());
                stats.setDamageDealt(raw.getDamageDealt());

                stats.setEntryAttempts(raw.getEntryAttempts());
                stats.setEntrySuccesses(raw.getEntrySuccesses());

                stats.setFlashesThrown(raw.getFlashesThrown());
                stats.setFlashesSuccessful(raw.getFlashesSuccessful());
                stats.setFlashesEnemiesBlinded(raw.getFlashesEnemiesBlinded());

                stats.setUtilityThrown(raw.getUtilityThrown());
                stats.setUtilityDamage(raw.getUtilityDamage());

                stats.setOneVXAttempts(raw.getOneVXAttempts());
                stats.setOneVXWins(raw.getOneVXWins());
            } else {
                assert stats != null;
                stats.setKills(stats.getKills() + raw.getKills());
                stats.setDeaths(stats.getDeaths() + raw.getDeaths());
                stats.setAssists(stats.getAssists() + raw.getAssists());

                stats.set_2ks(stats.get_2ks() + raw.get_2ks());
                stats.set_3ks(stats.get_3ks() + raw.get_3ks());
                stats.set_4ks(stats.get_4ks() + raw.get_4ks());
                stats.set_5ks(stats.get_5ks() + raw.get_5ks());

                stats.setBlindk(stats.getBlindk() + raw.getBlindk());
                stats.setBombk(stats.getBombk() + raw.getBombk());
                stats.setFiredmg(stats.getFiredmg() + raw.getFiredmg());
                stats.setUniquek(stats.getUniquek() + raw.getUniquek());
                stats.setDinks(stats.getDinks() + raw.getDinks());
                stats.setChickenk(stats.getChickenk() + raw.getChickenk());

                stats.setKillsWithHeadshot(stats.getKillsWithHeadshot()  + raw.getKillsWithHeadshot());
                stats.setKillsWithPistol(stats.getKillsWithPistol() + raw.getKillsWithPistol());
                stats.setKillsWithSniper(stats.getKillsWithSniper() + raw.getKillsWithSniper());
                stats.setDamageDealt(stats.getDamageDealt() + raw.getDamageDealt());

                stats.setEntryAttempts(stats.getEntryAttempts() + raw.getEntryAttempts());
                stats.setEntrySuccesses(stats.getEntrySuccesses() + raw.getEntrySuccesses());

                stats.setFlashesThrown(stats.getFlashesThrown() + raw.getFlashesThrown());
                stats.setFlashesSuccessful(stats.getFlashesSuccessful() + raw.getFlashesSuccessful());
                stats.setFlashesEnemiesBlinded(stats.getFlashesEnemiesBlinded() + raw.getFlashesEnemiesBlinded());

                stats.setUtilityThrown(stats.getUtilityThrown() + raw.getUtilityThrown());
                stats.setUtilityDamage(stats.getUtilityDamage() + raw.getUtilityDamage());

                stats.setOneVXAttempts(stats.getOneVXAttempts() + raw.getOneVXAttempts());
                stats.setOneVXWins(stats.getOneVXWins() + raw.getOneVXWins());
            }

            stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);

            playerGameStatsRepository.save(stats);
        }
    }

    @Override
    public Page<PlayerGameStats> getAllGameStats(Pageable pageable) {
        return playerGameStatsRepository.findAll(pageable);
    }

    @Override
    public void deleteAllGameStats() {
        playerGameStatsRepository.deleteAll();
    }

    @Override
    public PlayerGameStats getGlobalPlayerGameStats(String playerId) {
        return playerGameStatsRepository.findBySteamIdAndScope(playerId, "global");
    }

    @Override
    public PlayerGameStats getTournamentPlayerGameStats(String playerId, String tournamentId) {
        return playerGameStatsRepository.findBySteamIdAndScopeAndScopeId(playerId, "tournament", tournamentId);
    }

    @Override
    public PlayerGameStats getSeriesPlayerGameStats(String playerId, String seriesId) {
        return playerGameStatsRepository.findBySteamIdAndScopeAndScopeId(playerId, "series", seriesId);
    }

    @Override
    public PlayerGameStats getMatchPlayerGameStats(String playerId, String seriesId, int seriesOrder) {
        return playerGameStatsRepository.findBySteamIdAndScopeAndScopeIdAndSeriesOrder(playerId, "match", seriesId, seriesOrder);
    }

    @Override
    public void initStats(List<PlayerStatsRaw> players, String tournamentMatchId, String tournamentId, int seriesOrder) {
        for(PlayerStatsRaw player : players) {
            PlayerGameStats statsSeries = playerGameStatsRepository.findById(player.getSteamId()+ "-series-" + tournamentMatchId + "-X").orElse(
                    playerGameStatsRepository.save(PlayerGameStats.builder()
                            .id(player.getSteamId()+"-series-"+tournamentMatchId+"-X")
                            .steamId(player.getSteamId())
                            .scope("series")
                            .seriesOrder(-1)
                            .scopeId(tournamentMatchId)
                            .matchesPlayed(0)
                            .build()
                    )
            );
            PlayerGameStats statsMatch =
                    playerGameStatsRepository.save(PlayerGameStats.builder()
                            .id(player.getSteamId()+"-match-"+tournamentMatchId+"-"+seriesOrder)
                            .steamId(player.getSteamId())
                            .scope("match")
                            .scopeId(tournamentMatchId)
                            .seriesOrder(seriesOrder)
                            .matchesPlayed(0)
                            .build()
                    );
        }
    }

    @Override
    public void initGlobalStats(String playerId, String tournamentId) {
        playerGameStatsRepository.save(PlayerGameStats.builder()
                        .id(playerId+"-tournament-"+tournamentId+"-X")
                        .steamId(playerId)
                        .scope("tournament")
                        .seriesOrder(-1)
                        .scopeId(tournamentId)
                        .matchesPlayed(0)
                        .build());
        playerGameStatsRepository.save(playerGameStatsRepository.findById(playerId+"-global-global-X").orElseGet(() ->
                PlayerGameStats.builder()
                .id(playerId+"-global-global-X")
                .steamId(playerId)
                .scope("global")
                .seriesOrder(-1)
                .scopeId("global")
                .matchesPlayed(0)
                .build()
        ));
    }

}
