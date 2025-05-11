package com.vkr.analytics_service.repository.round;

import com.vkr.analytics_service.entity.round.RoundStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoundStatsRepository extends ElasticsearchRepository<RoundStats, String> {
    List<RoundStats> findAllByMatchIdAndMap(UUID matchId, String map);

    List<RoundStats> findAllByMatchIdAndSeriesOrder(UUID matchId, int seriesOrder);

    List<RoundStats> findAllByMatchId(UUID matchId);
}
