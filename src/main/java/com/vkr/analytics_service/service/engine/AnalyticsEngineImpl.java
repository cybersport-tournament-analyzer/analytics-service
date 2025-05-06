package com.vkr.analytics_service.service.engine;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.entity.player.overall.PlayerWeaponStats;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.repository.player.map.PlayerGameStatsOnMapRepository;
import com.vkr.analytics_service.repository.player.overall.PlayerGameStatsRepository;
import com.vkr.analytics_service.repository.player.overall.PlayerWeaponStatsRepository;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEngineImpl implements AnalyticsEngine {

    private final PlayerGameStatsRepository playerGameStatsRepository;
    private final PlayerGameStatsOnMapRepository playerGameStatsOnMapRepository;
    private final PlayerWeaponStatsRepository playerWeaponStatsRepository;
    private final RoundStatsRepository roundStatsRepository;

    @Override
    public void calculateBasicExtendedStats(String playerGameStatsId, Match match) {

        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();

        playerGameStats.setKd((double) playerGameStats.getKills() / playerGameStats.getDeaths());

        playerGameStats.setAdr((double) playerGameStats.getDamageDealt() / match.getRounds_played());

        playerGameStats.setHsp((double) playerGameStats.getKillsWithHeadshot() / playerGameStats.getKills());

        playerGameStats.setKpr((double) playerGameStats.getKills() / match.getRounds_played());

        playerGameStats.setApr((double) playerGameStats.getAssists() / match.getRounds_played());

        playerGameStats.setDpr((double) playerGameStats.getDamageDealt() / match.getRounds_played());

        playerGameStats.setClutchWinRate(
                (double) playerGameStats.getOneVXWins() / playerGameStats.getOneVXAttempts()
        );

        playerGameStats.setTotalKillingSpree(
                playerGameStats.get_2ks()
                        + playerGameStats.get_3ks()
                        + playerGameStats.get_4ks()
                        + playerGameStats.get_5ks()
        );

        playerGameStats.setFlashesSuccessfulRate(
                (double) playerGameStats.getFlashesSuccessful() / playerGameStats.getFlashesThrown()
        );

        playerGameStats.setFirstFeeds(
                playerGameStats.getEntryAttempts() - playerGameStats.getEntrySuccesses()
        );

        playerGameStats.setEntryKillsRate(
                (double) playerGameStats.getEntrySuccesses() / playerGameStats.getEntryAttempts()
        );

        playerGameStats.setEntryKillsPerRound(
                (double) playerGameStats.getEntrySuccesses() / match.getRounds_played()
        );

        playerGameStats.setFlashesPerRound(
                (double) playerGameStats.getFlashesThrown() / match.getRounds_played()
        );

        playerGameStatsRepository.save(playerGameStats);

    }

    @Override
    public boolean isUsefulRound(String playerGameStatsId, RoundStats roundStats, Match match) {
        String[] split = playerGameStatsId.split("-");
        String playerId = split[0];
        Match.Player currPlayer = match.getPlayers().get(0);
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();

        for (Match.Player player : match.getPlayers()) {
            if (player.getSteam_id_64().equals(playerId)) currPlayer = player;
        }

        boolean madeKill = playerGameStats.getKills() != currPlayer.getStats().getKills();
        boolean madeAssist = playerGameStats.getAssists() != currPlayer.getStats().getAssists();
        boolean survived = playerGameStats.getDeaths() == currPlayer.getStats().getDeaths();

        if (madeKill || madeAssist || survived) return true;

        List<KillEventDto> killEvents = roundStats.getKillEvents();
        for (KillEventDto event : killEvents) {
            if (event.getVictimSteamId().equals(playerId)) {
                String killerId = event.getKillerSteamId();
                int deathTime = Integer.parseInt(event.getTimestamp().split(":")[event.getTimestamp().split(":").length]);

                for (KillEventDto revenge : killEvents) {
                    if (revenge.getVictimSteamId().equals(killerId)) {
                        int revengeTime = Integer.parseInt(revenge.getTimestamp().split(":")[revenge.getTimestamp().split(":").length]);
                        if (Math.abs(revengeTime - deathTime) <= 3) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void calculateKast(String playerGameStatsId, int seriesOrder) {
        String[] split = playerGameStatsId.split("-");
        String playerId = split[0];
        String seriesId = split[2];
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();
        List<RoundStats> roundStats = roundStatsRepository.findAllByMatchIdAndSeriesOrder(UUID.fromString(seriesId), seriesOrder);
        int usefulRounds = 0;
        for (RoundStats roundStat : roundStats) {
            if (Boolean.TRUE.equals(roundStat.getUsefulRound().get(playerId))) usefulRounds++;
        }
        playerGameStats.setKast((double) usefulRounds / roundStats.size());
        playerGameStatsRepository.save(playerGameStats);
    }

    @Override
    public void calculateOverallRating(String playerGameStatsId) {
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();

        double impact = playerGameStats.getKpr() * 2 + 0.3 * playerGameStats.getApr() - 0.5;
        double rating = playerGameStats.getKast()
                + playerGameStats.getKpr() * 0.35
                + playerGameStats.getDpr() * 0.55
                + impact * 0.25
                + playerGameStats.getAdr() * 0.003
                + 0.15;
        //допилить под роль
        playerGameStats.setRating(rating);
        playerGameStatsRepository.save(playerGameStats);
    }

    @Override
    public void calculateBestWeapon(String playerGameStatsId) {
        String[] split = playerGameStatsId.split("-");
        String playerId = split[0];
        String seriesId = split[2];
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();
        List<PlayerWeaponStats> playerWeaponStats = playerWeaponStatsRepository.findAllBySteamIdAndScopeId(playerId, seriesId);

        String bestWeapon = playerWeaponStats.stream()
                .max(Comparator.comparingInt(PlayerWeaponStats::getKills))
                .map(PlayerWeaponStats::getWeapon)
                .orElse(null);

        playerGameStats.setBestWeapon(bestWeapon);
        playerGameStatsRepository.save(playerGameStats);
    }

//    @Override
//    public void calculateBasicExtendedStatsOnMap(String playerGameOnMapStatsId, Match match) {
//
//    }
//
//
//    @Override
//    public void calculateOverallRatingOnMap(String playerGameOnMapStatsId) {
//        PlayerGameStatsOnMap playerGameStats = playerGameStatsOnMapRepository.findById(playerGameOnMapStatsId).get();
//
//        double impact = playerGameStats.getKpr() * 2 + 0.3 * playerGameStats.getApr() - 0.5;
//        double rating = playerGameStats.getKast()
//                + playerGameStats.getKpr() * 0.35
//                + playerGameStats.getDpr() * 0.55
//                + impact * 0.25
//                + playerGameStats.getAdr() * 0.003
//                + 0.15;
//        //допилить под роль
//        playerGameStats.setRating(rating);
//        playerGameStatsOnMapRepository.save(playerGameStats);
//    }
//
//    @Override
//    public void calculateBestWeaponOnMap(String playerGameOnMapStatsId) {
//        String[] split = playerGameOnMapStatsId.split("-");
//        String playerId = split[0];
//        String seriesId = split[2];
//        PlayerGameStatsOnMap playerGameStats = playerGameStatsOnMapRepository.findById(playerGameOnMapStatsId).get();
//        List<PlayerWeaponStats> playerWeaponStats = playerWeaponStatsRepository.findAllBySteamIdAndScopeId(playerId, seriesId);
//
//        String bestWeapon = playerWeaponStats.stream()
//                .max(Comparator.comparingInt(PlayerWeaponStats::getKills))
//                .map(PlayerWeaponStats::getWeapon)
//                .orElse(null);
//
//        playerGameStats.setBestWeapon(bestWeapon);
//        playerGameStatsOnMapRepository.save(playerGameStats);
//    }
}
