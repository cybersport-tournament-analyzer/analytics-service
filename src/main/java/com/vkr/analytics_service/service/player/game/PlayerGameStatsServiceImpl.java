package com.vkr.analytics_service.service.player.game;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.PlayerGameStats;
import com.vkr.analytics_service.repository.player.PlayerGameStatsRepository;
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
            String playerGameStatsId = raw.getSteamId() + "-" + scope + "-" + scopeId;

            PlayerGameStats stats = playerGameStatsRepository.findById(playerGameStatsId).orElse(
                    PlayerGameStats.builder()
                            .id(playerGameStatsId)
                            .steamId(raw.getSteamId())
                            .scope(scope)
                            .scopeId(scopeId)
                            .side(raw.getTeam().equals("2") ? "CT" : "T")
                            .map(scope.equals("match") ? map : null)
                            .matchesPlayed(0)
                            .kills(0)
                            .deaths(0)
                            .assists(0)
                            .adr(0.0)
                            .hsp(0.0)
                            .score(0)
                            .mvps(0)
                            ._2ks(0)
                            ._3ks(0)
                            ._4ks(0)
                            ._5ks(0)
                            .clutches(0)
                            .firstKills(0)
                            .pistolk(0)
                            .sniperk(0)
                            .blindk(0)
                            .bombk(0)
                            .firedmg(0)
                            .uniquek(0)
                            .dinks(0)
                            .chickenk(0)
                            .killsWithHeadshot(0)
                            .killsWithPistol(0)
                            .killsWithSniper(0)
                            .damageDealt(0)
                            .entryAttempts(0)
                            .entrySuccesses(0)
                            .flashesThrown(0)
                            .flashesSuccessful(0)
                            .flashesEnemiesBlinded(0)
                            .utilityThrown(0)
                            .utilityDamage(0)
                            .oneVXAttempts(0)
                            .oneVXWins(0)
                            .build()
            );

            boolean isAggregateScope = scope.equals("global") || scope.equals("series") || scope.equals("tournament");

            if (isAggregateScope) {
                stats.setKills(stats.getKills() + raw.getKills());
                stats.setDeaths(stats.getDeaths() + raw.getDeaths());
                stats.setAssists(stats.getAssists() + raw.getAssists());
                stats.setScore(stats.getScore() + raw.getScore());
                stats.setMvps(stats.getMvps() + raw.getMvps());
                stats.setKd(stats.getDeaths() == 0 ? (double) stats.getKills() : (double) stats.getKills() / stats.getDeaths());

                stats.set_2ks(stats.get_2ks() + raw.get_2ks());
                stats.set_3ks(stats.get_3ks() + raw.get_3ks());
                stats.set_4ks(stats.get_4ks() + raw.get_4ks());
                stats.set_5ks(stats.get_5ks() + raw.get_5ks());

                stats.setClutches(stats.getClutches() + raw.getClutchk());
                stats.setFirstKills(stats.getFirstKills() + raw.getFirstk());

                stats.setPistolk(stats.getPistolk() + raw.getPistolk());
                stats.setSniperk(stats.getSniperk() + raw.getSniperk());
                stats.setBlindk(stats.getBlindk() + raw.getBlindk());
                stats.setBombk(stats.getBombk() + raw.getBombk());
                stats.setFiredmg(stats.getFiredmg() + raw.getFiredmg());
                stats.setUniquek(stats.getUniquek() + raw.getUniquek());
                stats.setDinks(stats.getDinks() + raw.getDinks());
                stats.setChickenk(stats.getChickenk() + raw.getChickenk());

                stats.setKillsWithHeadshot(stats.getKillsWithHeadshot() + raw.getKills_with_headshot());
                stats.setKillsWithPistol(stats.getKillsWithPistol() + raw.getKills_with_pistol());
                stats.setKillsWithSniper(stats.getKillsWithSniper() + raw.getKills_with_sniper());
                stats.setDamageDealt(stats.getDamageDealt() + raw.getDamage_dealt());

                stats.setEntryAttempts(stats.getEntryAttempts() + raw.getEntry_attempts());
                stats.setEntrySuccesses(stats.getEntrySuccesses() + raw.getEntry_successes());

                stats.setFlashesThrown(stats.getFlashesThrown() + raw.getFlashes_thrown());
                stats.setFlashesSuccessful(stats.getFlashesSuccessful() + raw.getFlashes_successful());
                stats.setFlashesEnemiesBlinded(stats.getFlashesEnemiesBlinded() + raw.getFlashes_enemies_blinded());

                stats.setUtilityThrown(stats.getUtilityThrown() + raw.getUtility_thrown());
                stats.setUtilityDamage(stats.getUtilityDamage() + raw.getUtility_damage());

                stats.setOneVXAttempts(stats.getOneVXAttempts() + raw.get_1vX_attempts());
                stats.setOneVXWins(stats.getOneVXWins() + raw.get_1vX_wins());

                int n = stats.getMatchesPlayed();

                stats.setAdr(((stats.getAdr() * n) + raw.getAdr()) / (n + 1));
                stats.setHsp(((stats.getHsp() * n) + raw.getHsp()) / (n + 1));
                stats.setMatchesPlayed(n + 1);
            } else {
                stats.setKills(raw.getKills());
                stats.setDeaths(raw.getDeaths());
                stats.setKd(stats.getDeaths() == 0 ? (double) stats.getKills() : (double) stats.getKills() / stats.getDeaths());
                stats.setAssists(raw.getAssists());
                stats.setScore(raw.getScore());
                stats.setMvps(raw.getMvps());

                stats.set_2ks(raw.get_2ks());
                stats.set_3ks(raw.get_3ks());
                stats.set_4ks(raw.get_4ks());
                stats.set_5ks(raw.get_5ks());

                stats.setClutches(raw.getClutchk());
                stats.setFirstKills(raw.getFirstk());

                stats.setPistolk(raw.getPistolk());
                stats.setSniperk(raw.getSniperk());
                stats.setBlindk(raw.getBlindk());
                stats.setBombk(raw.getBombk());
                stats.setFiredmg(raw.getFiredmg());
                stats.setUniquek(raw.getUniquek());
                stats.setDinks(raw.getDinks());
                stats.setChickenk(raw.getChickenk());

                stats.setKillsWithHeadshot(raw.getKills_with_headshot());
                stats.setKillsWithPistol(raw.getKills_with_pistol());
                stats.setKillsWithSniper(raw.getKills_with_sniper());
                stats.setDamageDealt(raw.getDamage_dealt());

                stats.setEntryAttempts(raw.getEntry_attempts());
                stats.setEntrySuccesses(raw.getEntry_successes());

                stats.setFlashesThrown(raw.getFlashes_thrown());
                stats.setFlashesSuccessful(raw.getFlashes_successful());
                stats.setFlashesEnemiesBlinded(raw.getFlashes_enemies_blinded());

                stats.setUtilityThrown(raw.getUtility_thrown());
                stats.setUtilityDamage(raw.getUtility_damage());

                stats.setOneVXAttempts(raw.get_1vX_attempts());
                stats.setOneVXWins(raw.get_1vX_wins());

                stats.setAdr(raw.getAdr());
                stats.setHsp(raw.getHsp());
                stats.setMatchesPlayed(1);
            }

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
    public Page<PlayerGameStats> getGlobalPlayerGameStats(String playerId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PlayerGameStats> getTournamentPlayerGameStats(String playerId, String tournamentId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PlayerGameStats> getSeriesPlayerGameStats(String playerId, String tournamentMatchId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PlayerGameStats> getMatchPlayerGameStats(String playerId, String tournamentMatchId, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerGameStats getGlobalPlayerGameStatsOnMap(String playerId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerGameStats getTournamentPlayerGameStatsOnMap(String playerId, String tournamentId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerGameStats getSeriesPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable) {
        return null;
    }

    @Override
    public PlayerGameStats getMatchPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map, Pageable pageable) {
        return null;
    }

//    private double calcKast(PlayerStatsRaw player) {
//
//    }

}
