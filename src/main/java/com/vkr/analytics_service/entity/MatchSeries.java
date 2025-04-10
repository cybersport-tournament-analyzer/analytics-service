package com.vkr.analytics_service.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchSeries {
    private Long id;
    private List<Match> matches;
    private String score;
    private List<PlayerAverages> playerAverages; // Изменено на список

    public MatchSeries(Long id, List<Match> matches,String score) {
        this.id = id;
        this.matches = matches;
        this.playerAverages = new ArrayList<>();
        this.score = score;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public List<Match> getMatches() { return matches; }
    public String getScore() { return score; }
    public List<PlayerAverages> getPlayerAverages() { return playerAverages; }
    public void setPlayerAverages(List<PlayerAverages> playerAverages) {
        this.playerAverages = playerAverages;
    }
}