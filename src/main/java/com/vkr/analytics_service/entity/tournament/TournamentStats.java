package com.vkr.analytics_service.entity.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@Document(indexName = "tournament_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class TournamentStats {
    @Id
    private String id; // tournamentId-map

    private String tournamentId;
    private String map;

    private int pickedCount;
    private int bannedCount;

    private double pickrate;
    private double banrate;

    @Field(type = FieldType.Object)
    private Map<String, Double> sideWinrate;
}
