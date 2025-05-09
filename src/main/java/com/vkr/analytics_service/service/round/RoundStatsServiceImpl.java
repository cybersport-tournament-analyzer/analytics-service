package com.vkr.analytics_service.service.round;

import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import com.vkr.analytics_service.mapper.RoundStatsMapper;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoundStatsServiceImpl implements RoundStatsService {

    private final RoundStatsRepository roundStatsRepository;
    private final RoundStatsMapper roundStatsMapper;

    @Override
    public Page<RoundStats> getAll(Pageable pageable) {
        return roundStatsRepository.findAll(pageable);
    }

    @Override
    public void deleteAll() {
        roundStatsRepository.deleteAll();
    }

    @Override
    public RoundStats save(RoundEndEvent roundEndEvent) {
        RoundStats roundStats = roundStatsMapper.toDocument(roundEndEvent);
        log.info("Saving round stats: {}", roundStats);
        roundStats.getKillEvents().forEach(killEvent -> {
            System.out.println(killEvent.getKillerSteamId());
            System.out.println(killEvent.getVictimSteamId());
            System.out.println(killEvent.getTimestamp());
        });
        return roundStatsRepository.save(roundStats);
    }
}
