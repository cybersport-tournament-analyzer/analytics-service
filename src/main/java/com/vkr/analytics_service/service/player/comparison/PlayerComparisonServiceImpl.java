package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.entity.player.comparisons.PlayerComparison;
import com.vkr.analytics_service.entity.player.comparisons.PlayerDuels;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.repository.player.comparison.PlayerComparisonRepository;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerComparisonServiceImpl implements PlayerComparisonService {

    private final DuelsService duelsService;
    private final PlayerGameStatsService playerGameStatsService;
    private final PlayerComparisonRepository playerComparisonRepository;

    @Override
    public void processPlayerComparison1v1(String player1Id, String player2Id, String scope, String scopeId, int seriesOrder) {
        PlayerDuels playerDuel = duelsService.findByPlayers(player1Id, player2Id, scope, scopeId, seriesOrder);
        if(playerDuel == null) { playerDuel = duelsService.findByPlayers(player2Id, player1Id, scope, scopeId, seriesOrder); }
        PlayerComparison playerComparison = playerComparisonRepository.findById(scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X")).orElse(null);

        PlayerGameStats playerGameStats1 = playerGameStatsService.getMatchPlayerGameStats(player1Id, scopeId, seriesOrder);
        PlayerGameStats playerGameStats2 = playerGameStatsService.getMatchPlayerGameStats(player2Id, scopeId, seriesOrder);

        double p1Score = calculatePlayerScore(
                playerGameStats1.getRating(), playerGameStats1.getKd(), playerGameStats1.getKpr(),
                playerGameStats1.getAdr(), playerGameStats1.getHsp(), playerGameStats1.getKast(),
                playerGameStats1.getEntrySuccesses(), playerGameStats1.getDamageDealt(),
                playerDuel.getPlayer1KillsPercent()
        );

        double p2Score = calculatePlayerScore(
                playerGameStats2.getRating(), playerGameStats2.getKd(), playerGameStats2.getKpr(),
                playerGameStats2.getAdr(), playerGameStats2.getHsp(), playerGameStats2.getKast(),
                playerGameStats2.getEntrySuccesses(), playerGameStats2.getDamageDealt(),
                playerDuel.getPlayer2KillsPercent()
        );


        PlayerComparison.PlayerComparison1v1 playerComparison1v1 = PlayerComparison.PlayerComparison1v1
                .builder()
                .player1Id(player1Id)
                .player1Adr(playerGameStats1.getAdr())
                .player1Kdr(playerGameStats1.getKd())
                .player1Damage(playerGameStats1.getDamageDealt())
                .player1Hsp(playerGameStats1.getHsp())
                .player1Kpr(playerGameStats1.getKpr())
                .player1KAST(playerGameStats1.getKast())
                .player1EntrySuccess(playerGameStats1.getEntrySuccesses())
                .player1Rating(playerGameStats1.getRating())
                .player2Id(player2Id)
                .player2Damage(playerGameStats2.getDamageDealt())
                .player2Adr(playerGameStats2.getAdr())
                .player2Hsp(playerGameStats2.getHsp())
                .player2KAST(playerGameStats2.getKast())
                .player2Kdr(playerGameStats2.getKd())
                .player2Kpr(playerGameStats2.getKpr())
                .player2Rating(playerGameStats2.getRating())
                .player2EntrySuccess(playerGameStats2.getEntrySuccesses())
                .duels(playerDuel)
                .player1Score(p1Score)
                .player2Score(p2Score)
                .build();

        assert playerComparison != null;
        playerComparison.getComparisons().add(playerComparison1v1);
        playerComparisonRepository.save(playerComparison);
    }

    @Override
    public void initPlayerComparison(String scope, String scopeId, int seriesOrder) {
        PlayerComparison playerComparison = PlayerComparison
                .builder()
                .id(scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X"))
                .scope(scope)
                .scopeId(scopeId)
                .comparisons(new ArrayList<>())
                .build();
        playerComparisonRepository.save(playerComparison);
    }

    @Override
    public PlayerComparison getPlayerComparisonMatch(String scopeId, int seriesOrder) {
        return playerComparisonRepository.findById("match-" + scopeId + "-" + seriesOrder).orElse(null);
    }

    @Override
    public PlayerComparison getPlayerComparisonSeries(String scopeId) {
        return playerComparisonRepository.findById("series-" + scopeId + "-X").orElse(null);
    }

    @Override
    public PlayerComparison.PlayerComparison1v1 getPlayerComparison1v1Match(String player1Id, String player2Id, String scopeId, int seriesOrder) {
        PlayerComparison playerComparison = playerComparisonRepository.findById("match-" + scopeId + "-" + seriesOrder).orElse(null);
        for (PlayerComparison.PlayerComparison1v1 comparison : playerComparison.getComparisons()) {
            if (comparison.getPlayer1Id().equals(player1Id) && comparison.getPlayer2Id().equals(player2Id)) {
                return comparison;
            }
        }
        return null;
    }

    @Override
    public PlayerComparison.PlayerComparison1v1 getPlayerComparison1v1Series(String player1Id, String player2Id, String scopeId) {
        PlayerComparison playerComparison = playerComparisonRepository.findById("series-" + scopeId + "-X").orElse(null);
        for (PlayerComparison.PlayerComparison1v1 comparison : playerComparison.getComparisons()) {
            if (comparison.getPlayer1Id().equals(player1Id) && comparison.getPlayer2Id().equals(player2Id)) {
                return comparison;
            }
        }
        return null;
    }

    private double calculatePlayerScore(
            double rating, double kdr, double kpr, double adr, double hsp,
            double kast, double entrySuccess, int damage, double duelWinPercent) {

        // нормализация значений
        double normalizedRating = rating / 2.0; //от 0 до ~2.0
        double normalizedKDR = Math.min(kdr / 2.0, 1.0); // от 0 до 2+
        double normalizedKPR = Math.min(kpr, 1.0); // от 0 до 1
        double normalizedADR = Math.min(adr / 150.0, 1.0); // до ~150
        double normalizedHSP = hsp / 100.0;
        double normalizedKAST = kast / 100.0;
        double normalizedEntry = entrySuccess / 100.0;
        double normalizedDamage = Math.min(damage / 3000.0, 1.0); // 3000 урона — высокая отметка
        double normalizedDuels = duelWinPercent / 100.0;

        // веса
        double score =
                0.25 * normalizedRating +
                        0.15 * normalizedKDR +
                        0.10 * normalizedKPR +
                        0.10 * normalizedADR +
                        0.05 * normalizedHSP +
                        0.10 * normalizedKAST +
                        0.10 * normalizedEntry +
                        0.05 * normalizedDamage +
                        0.10 * normalizedDuels;

        return Math.round(score * 10 * 10.0) / 10.0; // округление до 1 знака
    }
}
