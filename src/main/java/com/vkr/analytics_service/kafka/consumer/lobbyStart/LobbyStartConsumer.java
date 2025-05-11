package com.vkr.analytics_service.kafka.consumer.lobbyStart;

import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import com.vkr.analytics_service.exception.LobbyNotFoundException;
import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.lobbyStart.LobbyStartEvent;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class LobbyStartConsumer implements KafkaConsumer<LobbyStartEvent> {

    private final PlayerGameStatsService playerGameStatsService;


    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.lobby-start.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(LobbyStartEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed lobby start event: {}", event);
            playerGameStatsService.initStats(event.getTeam1(), event.getTeam2(), String.valueOf(event.getTournamentMatchId()), 0);
            ack.acknowledge();
        } catch (LobbyNotFoundException e) {
            log.warn("Lobby not found, will retry: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in consumer: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}
