package com.vkr.analytics_service.entity.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "tournament_meta_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class TournamentMetaStats {
    @Id
    private String id; // tournamentId-map

    private String tournamentId;
    private String map;

    private int bannedCount;
    private int matchesPlayed;

    private double pickRate;
    private double banRate;
}
