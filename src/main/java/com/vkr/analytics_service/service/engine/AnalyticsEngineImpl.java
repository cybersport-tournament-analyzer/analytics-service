package com.vkr.analytics_service.service.engine;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.repository.player.overall.PlayerGameStatsRepository;
import com.vkr.analytics_service.repository.player.overall.PlayerWeaponStatsRepository;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEngineImpl implements AnalyticsEngine {

    private PlayerGameStatsRepository playerGameStatsRepository;
    private PlayerWeaponStatsRepository playerWeaponStatsRepository;
    private RoundStatsRepository roundStatsRepository;

    @Override
    public void calculateBasicExtendedStats(String playerGameStatsId, Match match) {

        PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).get();

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
    public void calculateComplexExtendedStats(String playerGameStatsId) {
    }

    @Override
    public void calculateOverallRating(String playerGameStatsId) {

    }

    @Override
    public void calculateBestWeapon(String playerGameStatsId) {

    }

    @Override
    public void processSideWins() {

    }
}
