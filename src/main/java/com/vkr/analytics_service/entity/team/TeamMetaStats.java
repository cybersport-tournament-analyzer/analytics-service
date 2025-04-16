package com.vkr.analytics_service.entity.team;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "team_meta_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class TeamMetaStats {
    @Id
    private String id; // teamName-map-tournamentId

    private String teamName;
    private String tournamentId;
    private String map;

    private int bannedCount;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesLost;

    private double pickRate;
    private double banRate;
    private double winRate;
}
