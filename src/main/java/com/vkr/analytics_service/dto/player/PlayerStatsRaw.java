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
//    private String team; // 2 = CT, 3 = T

    //все что с объекта match (не нужно пересчитывать)

    private int kills;
    private int deaths;
    private int assists;

    private int _2ks;
    private int _3ks;
    private int _4ks;
    private int _5ks;

    private int killsWithHeadshot;
    private int killsWithPistol;
    private int killsWithSniper;
    private int damageDealt;

    private int entryAttempts;
    private int entrySuccesses;

    private int flashesThrown;
    private int flashesSuccessful;
    private int flashesEnemiesBlinded;

    private int utilityThrown;
    private int utilityDamage;

    private int oneVXAttempts;
    private int oneVXWins;

    //все что с консоли

    private int firstk;
    private int blindk;
    private int bombk;
    private int firedmg;
    private int uniquek;
    private int dinks;
    private int chickenk;

//    //расширенная которая рассчитывается
//
//    private double kast;
//    private double clutchWinRate; //entry_succ / entry_att
//    private int totalKillingSpree; //сумма xkS
//    private double flashesSuccessfulRate; //flashes_enemies / flashes_thrown
//    private double flashesPerRound; //flashes_thrown / roundsPlayed
//
//    private int riffleKills; //kills - (pistolk+sniperk+uniquek)
//    private int knifeKills; // с логов
//    private int zeusKills; // с логов
//    private String bestWeapon; // с логов киллов
//
//    private int firstFeeds; // entry_attempts - entry_successes
//    private double entryKillsRate; //entry_succ / entry_att
//    private double entryKillsPerRound; //entry_succ / roundPlayed
//
//    private double rating; //пока нет формулы рейтинга
//
//    // это получать с ивентов киллов
//    private int wallbangs;
//    private int noscopes;
//    private int smokeKills;
}
