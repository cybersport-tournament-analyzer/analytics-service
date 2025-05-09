package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.entity.player.comparisons.PlayerComparison;
import com.vkr.analytics_service.entity.player.comparisons.PlayerDuels;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.repository.player.comparison.PlayerComparisonRepository;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println("нашел дуэль");
        if (playerDuel == null) {
            playerDuel = duelsService.findByPlayers(player2Id, player1Id, scope, scopeId, seriesOrder);
        }

        String comparisonId = scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X");
        PlayerComparison playerComparison = playerComparisonRepository.findById(comparisonId).orElse(null);
        System.out.println("нашел сравнение");
        if (playerComparison == null) return; // можно логировать, если важно

        PlayerGameStats stats1, stats2;
        if ("match".equals(scope)) {
            stats1 = playerGameStatsService.getMatchPlayerGameStats(player1Id, scopeId, seriesOrder);
            stats2 = playerGameStatsService.getMatchPlayerGameStats(player2Id, scopeId, seriesOrder);
        } else {
            stats1 = playerGameStatsService.getSeriesPlayerGameStats(player1Id, scopeId);
            stats2 = playerGameStatsService.getSeriesPlayerGameStats(player2Id, scopeId);
        }

        double p1Score = calculateAdvancedPlayerScore(
                stats1.getRating(), stats1.getKd(), stats1.getKpr(), stats1.getAdr(), stats1.getHsp(),
                stats1.getKast(), stats1.getEntrySuccesses(), playerDuel.getPlayer1KillsPercent(),
                stats1.getClutchWinRate(), stats1.getOneVXAttempts(), stats1.getKillsWithHeadshot(),
                stats1.getFlashesEnemiesBlinded(), stats1.getFlashesSuccessful(), stats1.getEntryAttempts(),
                stats1.getUtilityDamagePerRound()
        );

        double p2Score = calculateAdvancedPlayerScore(
                stats2.getRating(), stats2.getKd(), stats2.getKpr(), stats2.getAdr(), stats2.getHsp(),
                stats2.getKast(), stats2.getEntrySuccesses(), playerDuel.getPlayer2KillsPercent(),
                stats2.getClutchWinRate(), stats2.getOneVXAttempts(), stats2.getKillsWithHeadshot(),
                stats2.getFlashesEnemiesBlinded(), stats2.getFlashesSuccessful(), stats2.getEntryAttempts(),
                stats2.getUtilityDamagePerRound()
        );

        PlayerComparison.PlayerStats1v1 playerStats1 = PlayerComparison.PlayerStats1v1.builder()
                .stats(List.of(
                        Map.of("playerId", player1Id),
                        Map.of("Средний урон/раунд", String.valueOf(stats1.getAdr())),
                        Map.of("К/Д", String.valueOf(stats1.getKd())),
                        Map.of("Общий урон", String.valueOf(stats1.getDamageDealt())),
                        Map.of("Киллы в голову", String.valueOf(stats1.getKillsWithHeadshot())),
                        Map.of("Всего серий убийств", String.valueOf(stats1.getTotalKillingSpree())),
                        Map.of("KAST", String.valueOf(stats1.getKast())),
                        Map.of("Рейтинг GPoint", String.valueOf(stats1.getRating())),
                        Map.of("Клатчи", String.valueOf(stats1.getOneVXAttempts())),
                        Map.of("Успешность клатчей", String.valueOf(stats1.getClutchWinRate())),
                        Map.of("Убийства в клатчах", String.valueOf(stats1.getOneVXWins())),
                        Map.of("Убийства ослепленных", String.valueOf(stats1.getBlindk())),
                        Map.of("Дабл-киллы", String.valueOf(stats1.get_2ks())),
                        Map.of("Трипл-киллы", String.valueOf(stats1.get_3ks())),
                        Map.of("Ультра-киллы", String.valueOf(stats1.get_4ks())),
                        Map.of("Эйсы", String.valueOf(stats1.get_5ks())),
                        Map.of("Брошено флешек", String.valueOf(stats1.getFlashesThrown())),
                        Map.of("Ослеплено врагов", String.valueOf(stats1.getFlashesEnemiesBlinded())),
                        Map.of("Эффективность флешек", String.valueOf(stats1.getFlashesSuccessfulRate())),
                        Map.of("Флешек за раунд", String.valueOf(stats1.getFlashesPerRound())),
                        Map.of("Кинуто гранат", String.valueOf(stats1.getUtilityThrown())),
                        Map.of("Урон от молотова", String.valueOf(stats1.getFiredmg())),
                        Map.of("Урон от гранат", String.valueOf(stats1.getUtilityDamage())),
                        Map.of("Урон гранатами/раунд", String.valueOf(stats1.getUtilityDamagePerRound())),
                        Map.of("Фраги с рифлы", String.valueOf(stats1.getRiffleKills())),
                        Map.of("Фраги с пистолетов", String.valueOf(stats1.getKillsWithPistol())),
                        Map.of("Фраги со снайперки", String.valueOf(stats1.getKillsWithSniper())),
                        Map.of("Всего энтри", String.valueOf(stats1.getEntryAttempts())),
                        Map.of("Процент энтри-киллов", String.valueOf(stats1.getEntryKillsRate())),
                        Map.of("Первая кровь", String.valueOf(stats1.getFirstk())),
                        Map.of("Первый фид", String.valueOf(stats1.getFirstFeeds())),
                        Map.of("Успех в энтри/раунд", String.valueOf(stats1.getEntryKillsPerRound()))
                ))
                .build();

        PlayerComparison.PlayerStats1v1 playerStats2 = PlayerComparison.PlayerStats1v1.builder()
                .stats(List.of(
                        Map.of("playerId", player2Id),
                        Map.of("Средний урон/раунд", String.valueOf(stats2.getAdr())),
                        Map.of("К/Д", String.valueOf(stats2.getKd())),
                        Map.of("Общий урон", String.valueOf(stats2.getDamageDealt())),
                        Map.of("Киллы в голову", String.valueOf(stats2.getKillsWithHeadshot())),
                        Map.of("Всего серий убийств", String.valueOf(stats2.getTotalKillingSpree())),
                        Map.of("KAST", String.valueOf(stats2.getKast())),
                        Map.of("Рейтинг GPoint", String.valueOf(stats2.getRating())),
                        Map.of("Клатчи", String.valueOf(stats2.getOneVXAttempts())),
                        Map.of("Успешность клатчей", String.valueOf(stats2.getClutchWinRate())),
                        Map.of("Убийства в клатчах", String.valueOf(stats2.getOneVXWins())),
                        Map.of("Убийства ослепленных", String.valueOf(stats2.getBlindk())),
                        Map.of("Дабл-киллы", String.valueOf(stats2.get_2ks())),
                        Map.of("Трипл-киллы", String.valueOf(stats2.get_3ks())),
                        Map.of("Ультра-киллы", String.valueOf(stats2.get_4ks())),
                        Map.of("Эйсы", String.valueOf(stats2.get_5ks())),
                        Map.of("Брошено флешек", String.valueOf(stats2.getFlashesThrown())),
                        Map.of("Ослеплено врагов", String.valueOf(stats2.getFlashesEnemiesBlinded())),
                        Map.of("Эффективность флешек", String.valueOf(stats2.getFlashesSuccessfulRate())),
                        Map.of("Флешек за раунд", String.valueOf(stats2.getFlashesPerRound())),
                        Map.of("Кинуто гранат", String.valueOf(stats2.getUtilityThrown())),
                        Map.of("Урон от молотова", String.valueOf(stats2.getFiredmg())),
                        Map.of("Урон от гранат", String.valueOf(stats2.getUtilityDamage())),
                        Map.of("Урон гранатами/раунд", String.valueOf(stats2.getUtilityDamagePerRound())),
                        Map.of("Фраги с рифлы", String.valueOf(stats2.getRiffleKills())),
                        Map.of("Фраги с пистолетов", String.valueOf(stats2.getKillsWithPistol())),
                        Map.of("Фраги со снайперки", String.valueOf(stats2.getKillsWithSniper())),
                        Map.of("Всего энтри", String.valueOf(stats2.getEntryAttempts())),
                        Map.of("Процент энтри-киллов", String.valueOf(stats2.getEntryKillsRate())),
                        Map.of("Первая кровь", String.valueOf(stats2.getFirstk())),
                        Map.of("Первый фид", String.valueOf(stats2.getFirstFeeds())),
                        Map.of("Успех в энтри/раунд", String.valueOf(stats2.getEntryKillsPerRound()))
                ))
                .build();

        PlayerComparison.PlayerComparison1v1 comparison1v1 = PlayerComparison.PlayerComparison1v1.builder()
                .duels(playerDuel)
                .player1Score(p1Score)
                .player2Score(p2Score)
                .playersStats(List.of(playerStats1, playerStats2))
                .build();

        if (playerComparison.getComparisons().isEmpty()) {
            playerComparison.setComparisons(new ArrayList<>());
        }
        playerComparison.getComparisons().add(comparison1v1);
        System.out.println("до " + playerComparison.getComparisons().size());
        playerComparisonRepository.save(playerComparison);
        System.out.println("после " + playerComparison.getComparisons().size());
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
        String comparisonId = "match-" + scopeId + "-" + seriesOrder;
        PlayerComparison playerComparison = playerComparisonRepository.findById(comparisonId).orElse(null);
        if (playerComparison == null || playerComparison.getComparisons() == null) return null;

        return findComparisonByPlayerIds(playerComparison.getComparisons(), player1Id, player2Id);
    }

    @Override
    public PlayerComparison.PlayerComparison1v1 getPlayerComparison1v1Series(String player1Id, String player2Id, String scopeId) {
        String comparisonId = "series-" + scopeId + "-X";
        PlayerComparison playerComparison = playerComparisonRepository.findById(comparisonId).orElse(null);
        if (playerComparison == null || playerComparison.getComparisons() == null) return null;

        return findComparisonByPlayerIds(playerComparison.getComparisons(), player1Id, player2Id);
    }

    private PlayerComparison.PlayerComparison1v1 findComparisonByPlayerIds(List<PlayerComparison.PlayerComparison1v1> comparisons,
                                                                           String player1Id, String player2Id) {
        for (PlayerComparison.PlayerComparison1v1 comparison : comparisons) {
            List<PlayerComparison.PlayerStats1v1> playersStats = comparison.getPlayersStats();
            if (playersStats == null || playersStats.size() != 2) continue;

            Set<String> extractedPlayerIds = playersStats.stream()
                    .map(stats1v1 -> extractPlayerId(stats1v1.getStats()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (extractedPlayerIds.contains(player1Id) && extractedPlayerIds.contains(player2Id)) {
                return comparison;
            }
        }
        return null;
    }

    private String extractPlayerId(List<Map<String, String>> statsList) {
        for (Map<String, String> statMap : statsList) {
            if (statMap.containsKey("playerId")) {
                return statMap.get("playerId");
            }
        }
        return null;
    }

    private double calculateAdvancedPlayerScore(
            double rating, double kdr, double kpr, double adr, double hsp, double kast,
            double entrySuccess, double duelWinPercent,
            double clutchSuccess, int clutches, int headshotKills,
            int flashesEnemies, double flashesSuccess, int entryAttempts, double utilDmgPerRound
    ) {
        // Стрельба и урон
        double normalizedRating = Math.min(rating / 2.0, 1.0);
        double normalizedKDR = Math.min(kdr / 2.0, 1.0);
        double normalizedKPR = Math.min(kpr, 1.0);
        double normalizedADR = Math.min(adr / 150.0, 1.0);
        double normalizedHSP = hsp / 100.0;
        double normalizedHeadshotKills = Math.min(Math.log1p(headshotKills) / 5.0, 1.0);

        // Командная игра
        double normalizedKAST = kast / 100.0;
        double normalizedFlashes = Math.min(flashesEnemies / 10.0, 1.0);
        double normalizedFlashSuccess = flashesSuccess / 100.0;
        double normalizedUtilDmg = Math.min(utilDmgPerRound / 30.0, 1.0);

        // Клатчи
        double normalizedClutchSuccess = clutchSuccess / 100.0;
        double normalizedClutches = Math.min(Math.sqrt(clutches) / 3.0, 1.0);

        // Энтри и дуэли
        double normalizedEntrySuccess = entrySuccess / 100.0;
        double normalizedEntryAttempts = Math.min(entryAttempts / 20.0, 1.0);
        double normalizedDuels = duelWinPercent / 100.0;

        // Финальный скор с весами
        double score =
                0.15 * normalizedRating +
                        0.10 * normalizedKDR +
                        0.10 * normalizedKPR +
                        0.10 * normalizedADR +
                        0.05 * normalizedHSP +
                        0.05 * normalizedHeadshotKills +
                        0.10 * normalizedKAST +
                        0.05 * normalizedFlashes +
                        0.05 * normalizedFlashSuccess +
                        0.05 * normalizedUtilDmg +
                        0.05 * normalizedClutchSuccess +
                        0.05 * normalizedClutches +
                        0.05 * normalizedEntrySuccess +
                        0.02 * normalizedEntryAttempts +
                        0.03 * normalizedDuels;

        return Math.round(score * 10 * 10.0) / 10.0;
    }
}
