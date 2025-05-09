package com.vkr.analytics_service.entity.player.comparisons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(indexName = "player_comparison")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PlayerComparison {

    @Id
    private String id; // scope-scopeId

    private String scope; //match / series
    private String scopeId;

    @Builder.Default
    private List<PlayerComparison1v1> comparisons = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class PlayerComparison1v1 {

        @Builder.Default
        private List<PlayerStats1v1> playersStats = new ArrayList<>();

        private double player1Score;
        private double player2Score;

        private PlayerDuels duels;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class PlayerStats1v1 {
        List<Map<String, String>> stats = new ArrayList<>();
    }

}