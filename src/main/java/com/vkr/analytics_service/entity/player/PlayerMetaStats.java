package com.vkr.analytics_service.entity.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "player_meta_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PlayerMetaStats {

    @Id
    private String id; // steamId-map-scope-scopeId

    private String steamId;
    private String map;
    private String scope;    // tournament / platform
    private String scopeId;

    private int played;
    private int won;
    private int picked;
    private int banned;

    private double winRate;
    private double pickRate;
    private double banRate;
}
