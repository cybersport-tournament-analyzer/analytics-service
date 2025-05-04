package com.vkr.analytics_service.service.player.game.overall;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.repository.player.overall.PlayerGameStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerGameStatsServiceImpl implements PlayerGameStatsService {

    private final PlayerGameStatsRepository playerGameStatsRepository;

    @Override
    public void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players, Match match) {
        for (PlayerStatsRaw raw : players) {
            String id = raw.getSteamId() + "-" + scope + "-" + scopeId + (scope.equals("match") ? "-" + map : "");

            PlayerGameStats stats = playerGameStatsRepository.findById(id).orElse(
                    PlayerGameStats.builder()
                            .id(id)
                            .steamId(raw.getSteamId())
                            .scope(scope)
                            .scopeId(scopeId)
                            .matchesPlayed(0)
                            .build()
            );

            stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);

            // Raw values
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


            // Derived metrics
            int deaths = stats.getDeaths() == 0 ? 1 : stats.getDeaths();
            stats.setKast(0); // calculateKast(raw)
            stats.setClutchWinRate((double) stats.getOneVXWins() / Math.max(stats.getOneVXAttempts(), 1));
            stats.setFlashesPerRound((double) stats.getFlashesThrown() / stats.getMatchesPlayed());
            stats.setFlashesSuccessfulRate((double) stats.getFlashesSuccessful() / Math.max(stats.getFlashesThrown(), 1));

            stats.setFirstFeeds(stats.getEntryAttempts() - stats.getEntrySuccesses());
            stats.setEntryKillsRate((double) stats.getEntrySuccesses() / Math.max(stats.getEntryAttempts(), 1));
            stats.setEntryKillsPerRound((double) stats.getEntrySuccesses() / stats.getMatchesPlayed());

            // TODO: Implement bestWeapon and rating logic

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
    public PlayerGameStats getMatchPlayerGameStats(String playerId, String matchId) {
        return playerGameStatsRepository.findBySteamIdAndScopeAndScopeId(playerId, "match", matchId);
    }

}
