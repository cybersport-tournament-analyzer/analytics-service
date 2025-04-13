package com.vkr.analytics_service.dto.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PlayerStatsRaw {

    private String steamId;
    private String team; // team2 = CT, team3 = T

    private int kills;
    private int deaths;
    private int assists;
    private int damage;
    private double hsp;
    private double kdr;
    private double adr;

    private int mvps;

    private int clutchk;
    private int firstk;

    private int pistolk;
    private int sniperk;
    private int blindk;
    private int bombk;
    private int firedmg;
    private int uniquek;
    private int dinks;
    private int chickenk;

    private int threeK;
    private int fourK;
    private int fiveK;
}
