package com.vkr.analytics_service.kafka.consumer.tournamentStart;

import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.tournamentStart.TournamentStartEvent;
import com.vkr.analytics_service.service.engine.handler.TournamentStartHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TournamentStartConsumer implements KafkaConsumer<TournamentStartEvent> {

    private final TournamentStartHandler tournamentStartHandler;

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.tournament-start.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(TournamentStartEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed tournament start event: {}", event);
            tournamentStartHandler.initStats(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error in round end consumer: {}", e.getMessage(), e);
        }
    }

}