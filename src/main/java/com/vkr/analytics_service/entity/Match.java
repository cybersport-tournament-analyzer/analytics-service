package com.vkr.analytics_service.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Обновленная модель матча
@Data
@NoArgsConstructor
public class Match {
    private Long id;
    private String mapName;
    private String score;
    private List<PlayerStats> playersStats; // Изменено на список

    public Match(Long id, String mapName, String score, List<PlayerStats> playersStats) {
        this.id = id;
        this.mapName = mapName;
        this.score = score;
        this.playersStats = playersStats;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public String getMapName() { return mapName; }
    public String getScore() { return score; }
    public List<PlayerStats> getPlayersStats() { return playersStats; }
}