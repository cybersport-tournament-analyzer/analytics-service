package com.vkr.analytics_service.repository.round;

import com.vkr.analytics_service.entity.round.RoundStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundStatsRepository extends ElasticsearchRepository<RoundStats, String> {
}
