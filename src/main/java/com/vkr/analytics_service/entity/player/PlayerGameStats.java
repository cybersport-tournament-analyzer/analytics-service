package com.vkr.analytics_service.entity.player;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "player_game_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PlayerGameStats {

    @Id
    private String id; // steamId-scope-scopeId

    private String steamId;
    private String scope;    // match / series / tournament / platform
    private String scopeId;  // matchId / matchId / tournamentId / "global"
    private String map;
    private String side;
    private int matchesPlayed;

    private int kills;
    private int deaths;
    private int assists;
    private double adr;
    private double hsp;

    private double kast;
    private double kd;

    private int score;
    private int mvps;

    private int _2ks;
    private int _3ks;
    private int _4ks;
    private int _5ks;

    private int clutches;
    private int firstKills;

    private int pistolk;
    private int sniperk;
    private int blindk;
    private int bombk;
    private int firedmg;
    private int uniquek;
    private int dinks;
    private int chickenk;

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
}
