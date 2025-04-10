package com.vkr.analytics_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAverages {
    private String playerId;
    private double avgKills;
    private double avgDeaths;
    private double avgAssists;
    private double avgDamage;


}