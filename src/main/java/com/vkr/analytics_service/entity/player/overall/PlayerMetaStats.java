package com.vkr.analytics_service.entity.player.overall;

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
    private String id; // steamId-scope-scopeId

    private String steamId;
    private String scope;    // tournament / global
    private String scopeId;

    private int played;
    private int won;
    private int lost;
    private double winRate; // won / played

}
