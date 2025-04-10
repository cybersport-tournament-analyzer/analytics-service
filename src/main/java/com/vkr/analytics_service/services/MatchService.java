package com.vkr.analytics_service.services;

import com.vkr.analytics_service.entity.Match;
import com.vkr.analytics_service.entity.MatchSeries;
import com.vkr.analytics_service.entity.PlayerAverages;
import com.vkr.analytics_service.entity.PlayerStats;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchService {
    private final List<Match> matches = new ArrayList<>();
    private final List<MatchSeries> series = new ArrayList<>();
    private Long nextMatchId = 1L;
    private Long nextSeriesId = 1L;

    public MatchService() {
        initializeDemoData();
    }

    private void calculateSeriesAverages(MatchSeries series) {
        Map<String, List<PlayerStats>> playerStatsMap = new HashMap<>();

        for (Match match : series.getMatches()) {
            for (PlayerStats stats : match.getPlayersStats()) {
                playerStatsMap.computeIfAbsent(stats.getPlayerId(),
                        k -> new ArrayList<>()).add(stats);
            }
        }

        List<PlayerAverages> averages = new ArrayList<>();
        playerStatsMap.forEach((playerId, statsList) -> {
            double avgKills = statsList.stream()
                    .mapToInt(PlayerStats::getKills).average().orElse(0);
            double avgDeaths = statsList.stream()
                    .mapToInt(PlayerStats::getDeaths).average().orElse(0);
            double avgAssists = statsList.stream()
                    .mapToInt(PlayerStats::getAssists).average().orElse(0);
            double avgDamage = statsList.stream()
                    .mapToDouble(PlayerStats::getDamage).average().orElse(0);

            averages.add(new PlayerAverages(playerId, avgKills, avgDeaths, avgAssists, avgDamage));
        });

        series.setPlayerAverages(averages);
    }
    private void initializeDemoData() {
        // Создаем демо-матчи
        List<PlayerStats> match1Stats = List.of(
                new PlayerStats("b160f59e-1b3e-431c-8e07-be642a745c85", "yofujitsu", 16, 14, 0, 15, 1587.5),
                new PlayerStats("9f088a5b-32c1-40ab-bc5f-2a4c7e689cdb", "Ulquiorra", 14, 16, 0, 9, 1450.0)
        );
        Match match1 = new Match(nextMatchId++, "Dust II", "16-14", match1Stats);

        List<PlayerStats> match2Stats = List.of(
                new PlayerStats("b160f59e-1b3e-431c-8e07-be642a745c85", "yofujitsu", 13, 5, 0, 12, 1250.0),
                new PlayerStats("9f088a5b-32c1-40ab-bc5f-2a4c7e689cdb", "Ulquiorra", 5, 13, 0, 8, 1300.0)
        );
        Match match2 = new Match(nextMatchId++, "Mirage", "13-5", match2Stats);

        matches.addAll(List.of(match1, match2));

        // Создаем демо-серию
        MatchSeries series = new MatchSeries(nextSeriesId++, List.of(match1, match2),"2-0");
        calculateSeriesAverages(series);
        this.series.add(series);
    }

    public List<Match> getAllMatches() {
        return new ArrayList<>(matches);
    }

    public Match getMatchById(Long id) {
        return matches.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Match not found"));
    }

    // Методы для серий
    public List<MatchSeries> getAllSeries() {
        return new ArrayList<>(series);
    }

    public MatchSeries getSeriesById(Long id) {
        return series.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Series not found"));
    }
}
