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
        private String playerId;

        private Map<String, Double> rating = new HashMap<>();
        private Map<String, Double> kdr = new HashMap<>();
        private Map<String, Double> adr = new HashMap<>();
        private Map<String, Double> kast = new HashMap<>();
        private Map<String, Integer> damage = new HashMap<>();
        private Map<String, Integer> clutches = new HashMap<>();
        private Map<String, Double> clutchesSuccess = new HashMap<>();
        private Map<String, Integer> clutchesKills = new HashMap<>();
        private Map<String, Integer> headshotKills = new HashMap<>();
        private Map<String, Integer> blindk = new HashMap<>();

        private Map<String, Integer> killingSprees = new HashMap<>();
        private Map<String, Integer> _2ks = new HashMap<>();
        private Map<String, Integer> _3ks = new HashMap<>();
        private Map<String, Integer> _4ks = new HashMap<>();
        private Map<String, Integer> _5ks = new HashMap<>();

        private Map<String, Integer> flashesCount = new HashMap<>();
        private Map<String, Integer> flashesEnemies = new HashMap<>();
        private Map<String, Double> flashesSuccess = new HashMap<>();
        private Map<String, Double> flashesPerRound = new HashMap<>();
        private Map<String, Integer> utilThrown = new HashMap<>();
        private Map<String, Integer> firedmg = new HashMap<>();
        private Map<String, Integer> utilDamage = new HashMap<>();
        private Map<String, Double> utilDamagePerRound = new HashMap<>();

        private Map<String, Integer> rifflek = new HashMap<>();
        private Map<String, Integer> pistolk = new HashMap<>();
        private Map<String, Integer> sniperk = new HashMap<>();

        private Map<String, Integer> entryAttempts = new HashMap<>();
        private Map<String, Double> entryKillsPercent = new HashMap<>();
        private Map<String, Integer> firstk = new HashMap<>();
        private Map<String, Integer> firstFeeds = new HashMap<>();
        private Map<String, Double> entrySuccessPerRound = new HashMap<>();
    }

}