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
    private String team; // 2 = CT, 3 = T

    private int kills;
    private int deaths;
    private int assists;
    private int damage;
    private double hsp;
    private double kdr;
    private double adr;

    private int mvps;
    private int score;

    private int _2ks;
    private int _3ks;
    private int _4ks;
    private int _5ks;

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

    private int kills_with_headshot;
    private int kills_with_pistol;
    private int kills_with_sniper;
    private int damage_dealt;

    private int entry_attempts;
    private int entry_successes;

    private int flashes_thrown;
    private int flashes_successful;
    private int flashes_enemies_blinded;

    private int utility_thrown;
    private int utility_damage;

    private int _1vX_attempts;
    private int _1vX_wins;
}
