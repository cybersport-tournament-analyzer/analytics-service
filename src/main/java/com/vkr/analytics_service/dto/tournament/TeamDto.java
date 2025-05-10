package com.vkr.analytics_service.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class TeamDto {
    private UUID id;
    private UUID tournamentId;
    private String teamName;
    private String flag;
    private String creatorSteamId;
    private int averageRating;
    private List<PlayerDto> players = new ArrayList<>();
}
