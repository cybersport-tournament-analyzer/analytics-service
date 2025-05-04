package com.vkr.analytics_service.kafka.consumer.roundEnd;

import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.consumer.KafkaConsumer;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import com.vkr.analytics_service.mapper.RoundStatsMapper;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import com.vkr.analytics_service.service.player.game.overall.PlayerGameStatsService;
import com.vkr.analytics_service.service.player.meta.overall.PlayerMetaStatsService;
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

    private final RoundStatsRepository roundStatsRepository;
    private final RoundStatsMapper roundStatsMapper;
    private final PlayerGameStatsService playerGameStatsService;
    private final PlayerMetaStatsService playerMetaStatsService;

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.round-end.name}", groupId = "${spring.data.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(RoundEndEvent event, Acknowledgment ack) {
        try {
            log.info("Consumed round end event: {}", event);
            RoundStats roundStats = roundStatsMapper.toDocument(event);
            roundStatsRepository.save(roundStats);
            playerMetaStatsService.updateMetaKillsStats(event.getKillEvents(), event.getMatch(), "match", String.valueOf(event.getTournamentMatchId()));
            playerGameStatsService.aggregate("match", String.valueOf(roundStats.getMatchId()), roundStats.getMap(), roundStats.getPlayers(), event.getMatch());
            System.out.println(event.getIsFinal());
            if(event.getIsFinal() == 2) {
                playerGameStatsService.aggregate("tournament", String.valueOf(roundStats.getTournamentId()), roundStats.getMap(), roundStats.getPlayers(), event.getMatch());
                playerGameStatsService.aggregate("series", String.valueOf(roundStats.getMatchId()), roundStats.getMap(), roundStats.getPlayers(), event.getMatch());
                playerGameStatsService.aggregate("global", "global", roundStats.getMap(), roundStats.getPlayers(), event.getMatch());

                playerMetaStatsService.updateMetaKillsStats(event.getKillEvents(), event.getMatch(), "series", String.valueOf(event.getTournamentMatchId()));
                playerMetaStatsService.updateMetaKillsStats(event.getKillEvents(), event.getMatch(), "tournament", String.valueOf(event.getTournamentId()));
                playerMetaStatsService.updateMetaKillsStats(event.getKillEvents(), event.getMatch(), "global", "global");
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error in round end consumer: {}", e.getMessage(), e);
        }
    }

}
