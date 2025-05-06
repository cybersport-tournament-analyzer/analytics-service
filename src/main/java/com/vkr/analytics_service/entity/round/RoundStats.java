package com.vkr.analytics_service.entity.round;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.RoundEndReasonDto;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(indexName = "round_stats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class RoundStats {

    @Id
    @UuidGenerator
    private UUID id;

    private UUID matchId;
    private UUID tournamentId;
    private int roundNumber;
    private String map;
    private int seriesOrder;

    @Field(type = FieldType.Nested)
    private List<PlayerStatsRaw> players;

    @Field(type = FieldType.Nested)
    private Map<String, Boolean> usefulRound;

    @Field(type = FieldType.Nested)
    private List<KillEventDto> killEvents;

    private RoundEndReasonDto roundEndReason;
}