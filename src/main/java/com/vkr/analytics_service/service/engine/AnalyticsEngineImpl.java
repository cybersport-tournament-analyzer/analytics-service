package com.vkr.analytics_service.service.engine;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.Match;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        playerGameStats.setKd((double) playerGameStats.getKills() / (playerGameStats.getDeaths() == 0 ? 1 : playerGameStats.getDeaths()));

        playerGameStats.setAdr((double) playerGameStats.getDamageDealt() / match.getRounds_played());

        playerGameStats.setHsp((double) playerGameStats.getKillsWithHeadshot() / (playerGameStats.getKills() == 0 ? 1 : playerGameStats.getKills()));

        playerGameStats.setKpr((double) playerGameStats.getKills() / match.getRounds_played());

        playerGameStats.setApr((double) playerGameStats.getAssists() / match.getRounds_played());

        playerGameStats.setDpr((double) playerGameStats.getDamageDealt() / match.getRounds_played());

        playerGameStats.setClutchWinRate(
                (double) playerGameStats.getOneVXWins() / (playerGameStats.getOneVXAttempts() == 0 ? 1 : playerGameStats.getOneVXAttempts())
        );

        playerGameStats.setTotalKillingSpree(
                playerGameStats.get_2ks()
                        + playerGameStats.get_3ks()
                        + playerGameStats.get_4ks()
                        + playerGameStats.get_5ks()
        );

        playerGameStats.setFlashesSuccessfulRate(
                (double) playerGameStats.getFlashesSuccessful() / (playerGameStats.getFlashesThrown() == 0 ? 1 : playerGameStats.getFlashesThrown())
        );

        playerGameStats.setFirstFeeds(
                playerGameStats.getEntryAttempts() - playerGameStats.getEntrySuccesses()
        );

        playerGameStats.setEntryKillsRate(
                (double) playerGameStats.getEntrySuccesses() / (playerGameStats.getEntryAttempts() == 0 ? 1 : playerGameStats.getEntryAttempts())
        );

        playerGameStats.setEntryKillsPerRound(
                (double) playerGameStats.getEntrySuccesses() / match.getRounds_played()
        );

        playerGameStats.setFlashesPerRound(
                (double) playerGameStats.getFlashesThrown() / match.getRounds_played()
        );

        playerGameStats.setUtilityDamagePerRound((double) playerGameStats.getUtilityDamage() / match.getRounds_played());

        playerGameStats.setRiffleKills(playerGameStats.getKills() - (playerGameStats.getKillsWithPistol() + playerGameStats.getKillsWithPistol() + playerGameStats.getUniquek()));

        playerGameStatsRepository.save(playerGameStats);

    }

    @Override
    public boolean isUsefulRound(String playerGameStatsId, RoundStats roundStats, Match match) {
        String[] split = playerGameStatsId.split("-");
        String playerId = split[0];
        Match.Player currPlayer = match.getPlayers().stream()
                .filter(player -> player.getSteam_id_64().equals(playerId))
                .findFirst()
                .orElse(null);
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();

        assert currPlayer != null;
        System.out.println("match kills = " + currPlayer.getStats().getKills());
        System.out.println("game stats kills = " + playerGameStats.getKills());

        boolean madeKill = playerGameStats.getKills() != currPlayer.getStats().getKills();
        boolean madeAssist = playerGameStats.getAssists() != currPlayer.getStats().getAssists();
        boolean survived = playerGameStats.getDeaths() == currPlayer.getStats().getDeaths();

        if (madeKill || madeAssist || survived) return true;

        List<KillEventDto> killEvents = roundStats.getKillEvents();
        for (KillEventDto event : killEvents) {

            if(event.getKillerSteamId().equals(playerId)) {
                if(event.isSmoke()) playerGameStats.setSmokeKills(playerGameStats.getSmokeKills() + 1);
                if(event.isNoscope()) playerGameStats.setNoscopes(playerGameStats.getNoscopes() + 1);
                if(event.isPenetrated()) playerGameStats.setWallbangs(playerGameStats.getWallbangs() + 1);
            }
            playerGameStatsRepository.save(playerGameStats);

            if (event.getVictimSteamId().equals(playerId)) {
                String killerId = event.getKillerSteamId();
                String[] parts = event.getTimestamp().split(":");
                int deathTime = Integer.parseInt(parts[parts.length - 1]);

                for (KillEventDto revenge : killEvents) {
                    if (revenge.getVictimSteamId().equals(killerId)) {
                        String[] revengeParts = revenge.getTimestamp().split(":");
                        int revengeTime = Integer.parseInt(revengeParts[revengeParts.length - 1]);
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
        String seriesId = split[2] + "-" + split[3] + "-" + split[4] + "-" + split[5] + "-" + split[6];
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();
        List<RoundStats> roundStats = roundStatsRepository.findAllByMatchIdAndSeriesOrder(UUID.fromString(seriesId), seriesOrder);
        int usefulRounds = 0;
        for (RoundStats roundStat : roundStats) {
            if (Boolean.TRUE.equals(roundStat.getUsefulRound().get(playerId))) usefulRounds++;
        }
        double kast = roundStats.isEmpty() ? 0.0 : (double) usefulRounds / roundStats.size();
        playerGameStats.setKast(kast);
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
        rating = rating / 15;
        //допилить под роль
        BigDecimal roundedRating = new BigDecimal(rating).setScale(2, RoundingMode.HALF_UP);
        playerGameStats.setRating(roundedRating.doubleValue());
        playerGameStatsRepository.save(playerGameStats);
    }

    @Override
    public void calculateBestWeapon(String playerGameStatsId) {
        String[] split = playerGameStatsId.split("-");
        String playerId = split[0];
        String seriesId;
        if(!split[1].equals("global")){
            seriesId = split[2] + "-" + split[3] + "-" + split[4] + "-" + split[5] + "-" + split[6];
        } else seriesId = "global";
        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();
        List<PlayerWeaponStats> playerWeaponStats = playerWeaponStatsRepository.findAllBySteamIdAndScopeId(playerId, seriesId);

        String bestWeapon = playerWeaponStats.stream()
                .max(Comparator.comparingInt(PlayerWeaponStats::getKills))
                .map(PlayerWeaponStats::getWeapon)
                .orElse(null);

        playerGameStats.setBestWeapon(bestWeapon);
        playerGameStatsRepository.save(playerGameStats);
    }
}
