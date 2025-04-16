package com.vkr.analytics_service.kafka.consumer.matchEnd;

import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.matchEnd.MatchEndEvent;
import com.vkr.analytics_service.service.player.meta.PlayerMetaStatsService;
import com.vkr.analytics_service.service.team.TeamStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEndConsumer implements KafkaConsumer<MatchEndEvent> {

    private final PlayerMetaStatsService playerMetaStatsService;
    private final TeamStatsService teamStatsService;

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.match-end.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(MatchEndEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed match end event: {}", event);
            playerMetaStatsService.updateMetaPlayedStats(event.getMatch(), "tournament", String.valueOf(event.getTournamentId()));
            playerMetaStatsService.updateMetaPlayedStats(event.getMatch(), "global", "global");

            teamStatsService.aggregateTeamMetaStats(event.getMatch(), event.getTournamentId(), event.getMatch().getTeam1().getName());
            teamStatsService.aggregateTeamMetaStats(event.getMatch(), event.getTournamentId(), event.getMatch().getTeam2().getName());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error in round end consumer: {}", e.getMessage(), e);
        }
    }

}