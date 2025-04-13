package com.vkr.analytics_service.dto.matchmaking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
public class RoundStatsDto {
    private int roundNumber;
    private int scoreT;
    private int scoreCT;
    private String map;
    private String server;
    private List<String> fields;
    private List<Map<String, String>> players;
}
