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

    private List<PlayerComparison1v1> comparisons = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class PlayerComparison1v1 {

        private String player1Id;
        private String player2Id;

        private double player1Rating;
        private double player2Rating;

        private double player1Kdr;
        private double player2Kdr;

        private double player1Kpr;
        private double player2Kpr;

        private int player1Damage;
        private int player2Damage;

        private double player1Adr;
        private double player2Adr;

        private double player1Hsp;
        private double player2Hsp;

        private double player1KAST;
        private double player2KAST;

        private double player1EntrySuccess;
        private double player2EntrySuccess;

        private Duels.PlayerDuels duels;
    }

}