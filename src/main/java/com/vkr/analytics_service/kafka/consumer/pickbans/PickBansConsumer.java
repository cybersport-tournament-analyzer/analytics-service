package com.vkr.analytics_service.kafka.consumer.pickbans;

import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.pickbans.PickBansEvent;
import com.vkr.analytics_service.service.engine.handler.PickBansHandler;
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

    private final PickBansHandler pickBansHandler;

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.pick-bans.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(PickBansEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed pick bans event: {}", event);
            pickBansHandler.handlePickBans(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error in round end consumer: {}", e.getMessage(), e);
        }
    }

}