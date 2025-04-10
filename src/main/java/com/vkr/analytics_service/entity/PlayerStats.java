package com.vkr.analytics_service.entity;

import lombok.Data;

// Модель статистики игрока в матче
@Data
public class PlayerStats {
    private String playerId;
    private String nickname;
    private int kills;
    private int deaths;
    private int assists;
    private int headshots;
    private double damage;

    public PlayerStats(String playerId, String nickname, int kills, int deaths,
                       int assists, int headshots, double damage) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.headshots = headshots;
        this.damage = damage;
    }

    // Геттеры
    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return nickname; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getAssists() { return assists; }
    public int getHeadshots() { return headshots; }
    public double getDamage() { return damage; }
}