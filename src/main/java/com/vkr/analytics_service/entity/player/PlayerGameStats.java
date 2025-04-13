package com.vkr.analytics_service.entity.player;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

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
    private String scope;    // match / tournament / platform
    private String scopeId;  // match / tournament / "global"
    private String map;
    private String side;

    private int kills;
    private int deaths;
    private int assists;
    private double adr;
    private double hsp;

    private int clutches;
    private int firstKills;

    private LocalDateTime updatedAt;
}
