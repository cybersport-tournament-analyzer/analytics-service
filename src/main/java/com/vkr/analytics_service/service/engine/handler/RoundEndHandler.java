package com.vkr.analytics_service.service.engine.handler;


import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import com.vkr.analytics_service.service.engine.AnalyticsEngine;
import com.vkr.analytics_service.service.player.comparison.DuelsService;
import com.vkr.analytics_service.service.player.comparison.PlayerComparisonService;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import com.vkr.analytics_service.service.player.weapon.PlayerWeaponStatsService;
import com.vkr.analytics_service.service.round.RoundStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoundEndHandler {

    private final RoundStatsService roundStatsService;
    private final AnalyticsEngine analyticsEngine;
    private final PlayerGameStatsService playerGameStatsService;
    private final DuelsService duelsService;
    private final PlayerComparisonService playerComparisonService;
    private final PlayerWeaponStatsService playerWeaponStatsService;
    private final RoundStatsRepository roundStatsRepository;

    public void handleRoundEnd(RoundEndEvent event) {

        System.out.println("смотрю что прилетело в ивенте!");
        event.getKillEvents().forEach(killEvent -> {
            System.out.println(killEvent.getKillerSteamId());
            System.out.println(killEvent.getVictimSteamId());
            System.out.println(killEvent.getTimestamp());
        });
        RoundStats roundStats = roundStatsService.save(event);

        System.out.println("ис файнал это " + event.getIsFinal());

        if (event.getRoundStats().getRoundNumber() == 1) {
            playerGameStatsService.initNextMatchStats(roundStats.getPlayers(), String.valueOf(roundStats.getMatchId()), roundStats.getSeriesOrder());

            duelsService.createDuel("match", String.valueOf(roundStats.getMatchId()), roundStats.getSeriesOrder());
            duelsService.createDuel("series", String.valueOf(roundStats.getMatchId()), -1);

            playerComparisonService.initPlayerComparison("match", String.valueOf(roundStats.getMatchId()), roundStats.getSeriesOrder());
            playerComparisonService.initPlayerComparison("series", String.valueOf(roundStats.getMatchId()), -1);
        }

        System.out.println(playerComparisonService.getPlayerComparisonSeries(String.valueOf(roundStats.getMatchId())).getId());

        System.out.println("сделал инит раундов и сравнений");

        for (PlayerStatsRaw player : roundStats.getPlayers()) {
            roundStats.getUsefulRound().put(player.getSteamId(), analyticsEngine.isUsefulRound(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + event.getSeriesOrder(), roundStats, event.getMatch()));
            System.out.println("ебашу юзфул раунд для " + player.getSteamId());
        }
        roundStatsRepository.save(roundStats);

        //расчет статы по оружиям
        playerWeaponStatsService.processKillEvents(roundStats.getKillEvents(), "global", "global", -1);
        playerWeaponStatsService.processKillEvents(roundStats.getKillEvents(), "match", String.valueOf(roundStats.getMatchId()), roundStats.getSeriesOrder());
        playerWeaponStatsService.processKillEvents(roundStats.getKillEvents(), "series", String.valueOf(roundStats.getMatchId()), -1);
        playerWeaponStatsService.processKillEvents(roundStats.getKillEvents(), "tournament", String.valueOf(roundStats.getTournamentId()), -1);

        System.out.println("заполнил стату по оружиям");
        for (PlayerStatsRaw player : roundStats.getPlayers()) {

            //расчет простой расширенной статы
            playerGameStatsService.addRound(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + roundStats.getSeriesOrder() );
            playerGameStatsService.addRound(player.getSteamId() + "-series-" + roundStats.getMatchId() + "-X");
            playerGameStatsService.addRound(player.getSteamId() + "-tournament-" + roundStats.getTournamentId() + "-X");
            playerGameStatsService.addRound(player.getSteamId() + "-global-global-X");
        }

        if (event.getIsFinal() == 2) {

            //расчет базовой статы с консоли и апишки
            playerGameStatsService.aggregate("match", String.valueOf(roundStats.getMatchId()), roundStats.getPlayers(), event.getMatch(), event.getSeriesOrder());
            playerGameStatsService.aggregate("tournament", String.valueOf(roundStats.getTournamentId()), roundStats.getPlayers(), event.getMatch(), -1);
            playerGameStatsService.aggregate("series", String.valueOf(roundStats.getMatchId()), roundStats.getPlayers(), event.getMatch(), -1);
            playerGameStatsService.aggregate("global", "global", roundStats.getPlayers(), event.getMatch(), -1);

            System.out.println("заполнил базовую гейм стату");

            for (PlayerStatsRaw player : roundStats.getPlayers()) {

                //расчет простой расширенной статы
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + roundStats.getSeriesOrder(), event.getMatch());
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "-series-" + roundStats.getMatchId() + "-X", event.getMatch());
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "-tournament-" + roundStats.getTournamentId() + "-X", event.getMatch());
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "-global-global-X", event.getMatch());

                System.out.println("заполнил расширенную гейм стату");

                //расчет лучшего оружия
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + roundStats.getSeriesOrder());
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "-series-" + roundStats.getMatchId() + "-X");
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "-tournament-" + roundStats.getTournamentId() + "-X");
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "-global-global-X");

                System.out.println("посчитал бест оружие");

                //расчет KAST
                analyticsEngine.calculateKast(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + roundStats.getSeriesOrder(), roundStats.getSeriesOrder());
                analyticsEngine.calculateKast(player.getSteamId() + "-series-" + roundStats.getMatchId() + "-X", -1);
//                analyticsEngine.calculateKast(player.getSteamId() + "tournament" + roundStats.getTournamentId(), roundStats.getSeriesOrder());
//                analyticsEngine.calculateKast(player.getSteamId() + "global-global", roundStats.getSeriesOrder());


                System.out.println("посчитал каст");

                //расчет рейтинга
                analyticsEngine.calculateOverallRating(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + roundStats.getSeriesOrder(), roundStats.getPlayers());
                analyticsEngine.calculateOverallRating(player.getSteamId() + "-series-" + roundStats.getMatchId() + "-X", roundStats.getPlayers());
//                analyticsEngine.calculateOverallRating(player.getSteamId() + "-tournament-" + roundStats.getTournamentId() + "-X");
//                analyticsEngine.calculateOverallRating(player.getSteamId() + "-global-global-X");

                System.out.println("посчитал рейтинг");
            }

            //шафл дуэлей
            List<Match.Player> team1Players = event.getMatch().getPlayers().stream()
                    .filter(p -> "team1".equals(p.getTeam()))
                    .toList();

            List<Match.Player> team2Players = event.getMatch().getPlayers().stream()
                    .filter(p -> "team2".equals(p.getTeam()))
                    .toList();

            System.out.println("шафлю дуэли начало");
            List<RoundStats> allRounds = roundStatsRepository.findAllByMatchIdAndSeriesOrder(roundStats.getMatchId(), roundStats.getSeriesOrder());

            for (Match.Player p1 : team1Players) {
                System.out.println("шафлю для " + p1.getNickname_override());
                for (Match.Player p2 : team2Players) {
                    System.out.println("шафлю для " + p1.getNickname_override() + " и " + p2.getNickname_override());
                    duelsService.processDuels(p1.getSteam_id_64(),
                            p2.getSteam_id_64(),
                            "match",
                            String.valueOf(roundStats.getMatchId()),
                            allRounds,
                            roundStats.getSeriesOrder());

                    duelsService.processDuels(p1.getSteam_id_64(),
                            p2.getSteam_id_64(),
                            "series",
                            String.valueOf(roundStats.getMatchId()),
                            allRounds,
                            -1);
                }
            }

            List<Match.Player> team1Players1 = event.getMatch().getPlayers().stream()
                    .filter(p -> "team1".equals(p.getTeam()))
                    .toList();

            List<Match.Player> team2Players2 = event.getMatch().getPlayers().stream()
                    .filter(p -> "team2".equals(p.getTeam()))
                    .toList();

            System.out.println("ебашим сравнения");

            for (Match.Player p1 : team1Players1) {
                System.out.println("шафлю сравнение для ");
                for (Match.Player p2 : team2Players2) {
                    System.out.println("шафлю сравнение для " + p1.getNickname_override() + " и " + p2.getNickname_override());
                    playerComparisonService.processPlayerComparison1v1(
                            p1.getSteam_id_64(),
                            p2.getSteam_id_64(),
                            "match",
                            String.valueOf(roundStats.getMatchId()),
                            roundStats.getSeriesOrder());

                    playerComparisonService.processPlayerComparison1v1(
                            p1.getSteam_id_64(),
                            p2.getSteam_id_64(),
                            "series",
                            String.valueOf(roundStats.getMatchId()),
                            -1);
                }
            }
        }
    }
}
