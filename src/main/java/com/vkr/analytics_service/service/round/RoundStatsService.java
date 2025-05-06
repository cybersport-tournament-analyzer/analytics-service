package com.vkr.analytics_service.service.round;

import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoundStatsService {

    Page<RoundStats> getAll(Pageable pageable);

    void deleteAll();

    RoundStats save(RoundEndEvent roundEndEvent);
}
