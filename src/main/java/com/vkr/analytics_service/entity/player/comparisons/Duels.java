package com.vkr.analytics_service.entity.player.comparisons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "player_duels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class Duels {

    @Id
    private String id; // scope-scopeId

    private String scope; //match / series
    private String scopeId;

    private List<PlayerDuels> duels = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class PlayerDuels {
        private String player1Id;
        private String player2Id;

        private int player1Kills;
        private int player2Kills;

        private double player1KillsPercent;
        private double player2KillsPercent;
    }

}
