package com.vkr.analytics_service.kafka.consumer.roundEnd;

import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import com.vkr.analytics_service.service.engine.handler.RoundEndHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class RoundEndConsumer implements KafkaConsumer<RoundEndEvent> {

    private final RoundEndHandler roundEndHandler;

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.round-end.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(RoundEndEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed round end event: {}", event);
            roundEndHandler.handleRoundEnd(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error in round end consumer: {}", e.getMessage(), e);
        }
    }

}
