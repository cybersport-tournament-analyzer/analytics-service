package com.vkr.analytics_service.kafka.consumer.pickbans;

import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.pickbans.PickBansEvent;
import com.vkr.analytics_service.service.team.TeamStatsService;
import com.vkr.analytics_service.service.tournament.TournamentStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class PickBansConsumer implements KafkaConsumer<PickBansEvent> {

    private final TournamentStatsService tournamentStatsService;
    private final TeamStatsService teamStatsService;

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.pick-bans.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(PickBansEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed pick bans event: {}", event);
            tournamentStatsService.aggregateTournamentStats(event.getPickbans(), event.getTournamentId());

            teamStatsService.aggregateTeamPickBanStats(event.getPickbans(), event.getTournamentId(), event.getTeam1Name(), "team1");
            teamStatsService.aggregateTeamPickBanStats(event.getPickbans(), event.getTournamentId(), event.getTeam2Name(), "team2");
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error in round end consumer: {}", e.getMessage(), e);
        }
    }

}