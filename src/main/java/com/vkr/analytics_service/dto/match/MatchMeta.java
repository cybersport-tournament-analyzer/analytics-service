package com.vkr.analytics_service.dto.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
public class MatchMeta {
    private UUID matchId;
    private UUID tournamentId;
    private String map;
    private List<String> playerSteamIds;
    private List<String> winnerSteamIds;

    private String pickedBy;
    private String bannedBy;

    private boolean wasPicked;
    private boolean wasBanned;

    private double pickResult; // 1.0 — win пикающей
    private double banResult;

    private double ctWinRate;
    private double tWinRate;
}
