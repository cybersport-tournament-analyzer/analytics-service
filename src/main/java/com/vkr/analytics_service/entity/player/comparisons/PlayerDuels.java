package com.vkr.analytics_service.entity.player.comparisons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PlayerDuels {
    private String player1Id;
    private String player2Id;

    private int player1Kills;
    private int player2Kills;

    private double player1KillsPercent;
    private double player2KillsPercent;
}
