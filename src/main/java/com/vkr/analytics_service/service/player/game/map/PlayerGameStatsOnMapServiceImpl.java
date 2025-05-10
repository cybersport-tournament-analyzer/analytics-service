package com.vkr.analytics_service.service.player.game.map;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.repository.player.map.PlayerGameStatsOnMapRepository;
import com.vkr.analytics_service.service.engine.AnalyticsEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerGameStatsOnMapServiceImpl implements PlayerGameStatsOnMapService {

    private final PlayerGameStatsOnMapRepository playerGameStatsOnMapRepository;
    private final AnalyticsEngine analyticsEngine;

    @Override
    public void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players, Match match) {
        for (PlayerStatsRaw raw : players) {
            String id = raw.getSteamId() + "-" + scope + "-" + scopeId + "-X";

            PlayerGameStatsOnMap stats = playerGameStatsOnMapRepository.findById(id).orElse(
                    PlayerGameStatsOnMap.builder()
                            .id(id)
                            .map(map)
                            .steamId(raw.getSteamId())
                            .scope(scope)
                            .scopeId(scopeId)
                            .matchesPlayed(0)
                            .build()
            );

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

            stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);

            playerGameStatsOnMapRepository.save(stats);

            analyticsEngine.calculateBasicExtendedStats(id, match);
            analyticsEngine.calculateKast(id, 0);
            analyticsEngine.calculateBestWeapon(id);
//            analyticsEngine.calculateOverallRating(id);

            playerGameStatsOnMapRepository.save(stats);
        }
    }

    @Override
    public PlayerGameStatsOnMap getGlobalPlayerGameStatsOnMap(String playerId, String map) {
        return playerGameStatsOnMapRepository.findBySteamIdAndMapAndScopeId(playerId, map, "global");
    }

    @Override
    public PlayerGameStatsOnMap getTournamentPlayerGameStatsOnMap(String playerId, String tournamentId, String map) {
        return playerGameStatsOnMapRepository.findBySteamIdAndMapAndScopeId(playerId, map, tournamentId);
    }

    @Override
    public PlayerGameStatsOnMap getSeriesPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map) {
        return playerGameStatsOnMapRepository.findBySteamIdAndMapAndScopeId(playerId, map, tournamentMatchId);
    }
}
