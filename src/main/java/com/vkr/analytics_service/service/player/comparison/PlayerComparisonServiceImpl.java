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
                .playerId(player1Id)
                .adr(Map.of("Средний урон/раунд", stats1.getAdr()))
                .kdr(Map.of("К/Д", stats1.getKd()))
                .damage(Map.of("Общий урон", stats1.getDamageDealt()))
                .headshotKills(Map.of("Киллы в голову", stats1.getKillsWithHeadshot()))
                .killingSprees(Map.of("Всего серий убийств", stats1.getTotalKillingSpree()))
                .kast(Map.of("KAST", stats1.getKast()))
                .rating(Map.of("Рейтинг GPoint", stats1.getRating()))
                .clutches(Map.of("Клатчи", stats1.getOneVXAttempts()))
                .clutchesSuccess(Map.of("Успешность клатчей", stats1.getClutchWinRate()))
                .clutchesKills(Map.of("Убийства в клатчах", stats1.getOneVXWins()))
                .blindk(Map.of("Убийства ослепленных", stats1.getBlindk()))
                ._2ks(Map.of("Дабл-киллы", stats1.get_2ks()))
                ._3ks(Map.of("Трипл-киллы", stats1.get_3ks()))
                ._4ks(Map.of("Ультра-киллы", stats1.get_4ks()))
                ._5ks(Map.of("Эйсы", stats1.get_5ks()))
                .flashesCount(Map.of("Брошено флешек", stats1.getFlashesThrown()))
                .flashesEnemies(Map.of("Ослеплено врагов", stats1.getFlashesEnemiesBlinded()))
                .flashesSuccess(Map.of("Эффективность флешек", stats1.getFlashesSuccessfulRate()))
                .flashesPerRound(Map.of("Флешек за раунд", stats1.getFlashesPerRound()))
                .utilThrown(Map.of("Кинуто гранат", stats1.getUtilityThrown()))
                .firedmg(Map.of("Урон от молотова", stats1.getFiredmg()))
                .utilDamage(Map.of("Урон от гранат", stats1.getUtilityDamage()))
                .utilDamagePerRound(Map.of("Урон гранатами/раунд", stats1.getUtilityDamagePerRound()))
                .rifflek(Map.of("Фраги с рифлы", stats1.getRiffleKills()))
                .pistolk(Map.of("Фраги с пистолетов", stats1.getKillsWithPistol()))
                .sniperk(Map.of("Фраги со снайперки", stats1.getKillsWithSniper()))
                .entryAttempts(Map.of("Всего энтри", stats1.getEntryAttempts()))
                .entryKillsPercent(Map.of("Процент энтри-киллов", stats1.getEntryKillsRate()))
                .firstk(Map.of("Первая кровь", stats1.getFirstk()))
                .firstFeeds(Map.of("Первый фид", stats1.getFirstFeeds()))
                .entrySuccessPerRound(Map.of("Успех в энтри/раунд", stats1.getEntryKillsPerRound()))
                .build();

        PlayerComparison.PlayerStats1v1 playerStats2 = PlayerComparison.PlayerStats1v1.builder()
                .playerId(player2Id)
                .adr(Map.of("Средний урон/раунд", stats2.getAdr()))
                .kdr(Map.of("К/Д", stats2.getKd()))
                .damage(Map.of("Общий урон", stats2.getDamageDealt()))
                .headshotKills(Map.of("Киллы в голову", stats2.getKillsWithHeadshot()))
                .killingSprees(Map.of("Всего серий убийств", stats2.getTotalKillingSpree()))
                .kast(Map.of("KAST", stats2.getKast()))
                .rating(Map.of("Рейтинг GPoint", stats2.getRating()))
                .clutches(Map.of("Клатчи", stats2.getOneVXAttempts()))
                .clutchesSuccess(Map.of("Успешность клатчей", stats2.getClutchWinRate()))
                .clutchesKills(Map.of("Убийства в клатчах", stats2.getOneVXWins()))
                .blindk(Map.of("Убийства ослепленных", stats2.getBlindk()))
                ._2ks(Map.of("Дабл-киллы", stats2.get_2ks()))
                ._3ks(Map.of("Трипл-киллы", stats2.get_3ks()))
                ._4ks(Map.of("Ультра-киллы", stats2.get_4ks()))
                ._5ks(Map.of("Эйсы", stats2.get_5ks()))
                .flashesCount(Map.of("Брошено флешек", stats2.getFlashesThrown()))
                .flashesEnemies(Map.of("Ослеплено врагов", stats2.getFlashesEnemiesBlinded()))
                .flashesSuccess(Map.of("Эффективность флешек", stats2.getFlashesSuccessfulRate()))
                .flashesPerRound(Map.of("Флешек за раунд", stats2.getFlashesPerRound()))
                .utilThrown(Map.of("Кинуто гранат", stats2.getUtilityThrown()))
                .firedmg(Map.of("Урон от молотова", stats2.getFiredmg()))
                .utilDamage(Map.of("Урон от гранат", stats2.getUtilityDamage()))
                .utilDamagePerRound(Map.of("Урон гранатами/раунд", stats2.getUtilityDamagePerRound()))
                .rifflek(Map.of("Фраги с рифлы", stats2.getRiffleKills()))
                .pistolk(Map.of("Фраги с пистолетов", stats2.getKillsWithPistol()))
                .sniperk(Map.of("Фраги со снайперки", stats2.getKillsWithSniper()))
                .entryAttempts(Map.of("Всего энтри", stats2.getEntryAttempts()))
                .entryKillsPercent(Map.of("Процент энтри-киллов", stats2.getEntryKillsRate()))
                .firstk(Map.of("Первая кровь", stats2.getFirstk()))
                .firstFeeds(Map.of("Первый фид", stats2.getFirstFeeds()))
                .entrySuccessPerRound(Map.of("Успех в энтри/раунд", stats2.getEntryKillsPerRound()))
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
            if (comparison.getPlayersStats() == null || comparison.getPlayersStats().size() != 2) continue;

            Set<String> playerIds = comparison.getPlayersStats().stream()
                    .map(PlayerComparison.PlayerStats1v1::getPlayerId)
                    .collect(Collectors.toSet());

            if (playerIds.contains(player1Id) && playerIds.contains(player2Id)) {
                return comparison;
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
