package com.vkr.analytics_service.service.engine.handler;


import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import com.vkr.analytics_service.service.engine.AnalyticsEngine;
import com.vkr.analytics_service.service.player.comparison.DuelsService;
import com.vkr.analytics_service.service.player.comparison.PlayerComparisonService;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import com.vkr.analytics_service.service.round.RoundStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundEndHandler {

    private final RoundStatsService roundStatsService;
    private final AnalyticsEngine analyticsEngine;
    private final PlayerGameStatsService playerGameStatsService;
    private final DuelsService duelsService;
    private final PlayerComparisonService playerComparisonService;

    public void handleRoundEnd(RoundEndEvent event) {
        RoundStats roundStats = roundStatsService.save(event);
        System.out.println(event.getIsFinal());

        if (roundStats.getRoundNumber() == 1) {
            playerGameStatsService.initStats(roundStats.getPlayers(), String.valueOf(roundStats.getMatchId()), String.valueOf(roundStats.getTournamentId()), event.getSeriesOrder());

            duelsService.createDuel("match", String.valueOf(roundStats.getMatchId()), roundStats.getSeriesOrder());
            duelsService.createDuel("series", String.valueOf(roundStats.getMatchId()), -1);

            playerComparisonService.initPlayerComparison("match", String.valueOf(roundStats.getMatchId()), roundStats.getSeriesOrder());
            playerComparisonService.initPlayerComparison("series", String.valueOf(roundStats.getMatchId()), -1);
        }

        for (PlayerStatsRaw player : roundStats.getPlayers()) {
            roundStats.getUsefulRound().put(player.getSteamId(), analyticsEngine.isUsefulRound(player.getSteamId() + "-match-" + roundStats.getMatchId() + "-" + event.getSeriesOrder(), roundStats, event.getMatch()));
        }

        if (event.getIsFinal() == 2) {
            playerGameStatsService.aggregate("match", String.valueOf(roundStats.getMatchId()), roundStats.getPlayers(), event.getMatch(), event.getSeriesOrder());
            playerGameStatsService.aggregate("tournament", String.valueOf(roundStats.getTournamentId()), roundStats.getPlayers(), event.getMatch(), event.getSeriesOrder());
            playerGameStatsService.aggregate("series", String.valueOf(roundStats.getMatchId()), roundStats.getPlayers(), event.getMatch(), event.getSeriesOrder());
            playerGameStatsService.aggregate("global", "global", roundStats.getPlayers(), event.getMatch(), event.getSeriesOrder());

            for (PlayerStatsRaw player : roundStats.getPlayers()) {

                //расчет простой расширенной статы
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "match" + roundStats.getMatchId() + roundStats.getSeriesOrder(), event.getMatch());
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "series" + roundStats.getMatchId(), event.getMatch());
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "tournament" + roundStats.getTournamentId(), event.getMatch());
                analyticsEngine.calculateBasicExtendedStats(player.getSteamId() + "global-global", event.getMatch());


                //расчет лучшего оружия
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "match" + roundStats.getMatchId() + roundStats.getSeriesOrder());
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "series" + roundStats.getMatchId());
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "tournament" + roundStats.getTournamentId());
                analyticsEngine.calculateBestWeapon(player.getSteamId() + "global-global");

                //расчет KAST
                analyticsEngine.calculateKast(player.getSteamId() + "match" + roundStats.getMatchId() + roundStats.getSeriesOrder(), roundStats.getSeriesOrder());
                analyticsEngine.calculateKast(player.getSteamId() + "series" + roundStats.getMatchId(), roundStats.getSeriesOrder());
                analyticsEngine.calculateKast(player.getSteamId() + "tournament" + roundStats.getTournamentId(), roundStats.getSeriesOrder());
                analyticsEngine.calculateKast(player.getSteamId() + "global-global", roundStats.getSeriesOrder());


                //расчет рейтинга
                analyticsEngine.calculateOverallRating(player.getSteamId() + "match" + roundStats.getMatchId() + roundStats.getSeriesOrder());
                analyticsEngine.calculateOverallRating(player.getSteamId() + "series" + roundStats.getMatchId());
                analyticsEngine.calculateOverallRating(player.getSteamId() + "tournament" + roundStats.getTournamentId());
                analyticsEngine.calculateOverallRating(player.getSteamId() + "global-global");

            }
            for (Match.Player player : event.getMatch().getPlayers()) {
                if (player.getTeam().equals("team1")) {
                    for (Match.Player player2 : event.getMatch().getPlayers()) {
                        if (player2.getTeam().equals("team2")) {
                            duelsService.processDuels(player.getSteam_id_64(),
                                    player2.getSteam_id_64(),
                                    "match",
                                    String.valueOf(roundStats.getMatchId()),
                                    roundStats.getKillEvents(),
                                    roundStats.getSeriesOrder());
                            duelsService.processDuels(player.getSteam_id_64(),
                                    player2.getSteam_id_64(),
                                    "series",
                                    String.valueOf(roundStats.getMatchId()),
                                    roundStats.getKillEvents(),
                                    -1);
                            playerComparisonService.processPlayerComparison1v1(player.getSteam_id_64(),
                                    player2.getSteam_id_64(),
                                    "match",
                                    String.valueOf(roundStats.getMatchId()),
                                    roundStats.getSeriesOrder());
                            playerComparisonService.processPlayerComparison1v1(player.getSteam_id_64(),
                                    player2.getSteam_id_64(),
                                    "series",
                                    String.valueOf(roundStats.getMatchId()),
                                    -1);
                        }
                    }
                }
            }
        }
    }
}
