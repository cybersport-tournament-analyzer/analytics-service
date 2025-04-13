package com.vkr.analytics_service.kafka.event.roundEnd;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.dto.matchmaking.RoundEndReasonDto;
import com.vkr.analytics_service.dto.matchmaking.RoundStatsDto;
import com.vkr.analytics_service.kafka.event.KafkaEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class RoundEndEvent implements KafkaEvent {
    private UUID tournamentMatchId;
    private UUID tournamentId;
    private RoundStatsDto roundStats;
    private RoundEndReasonDto roundEndReason;
    private List<KillEventDto> killEvents;
}
