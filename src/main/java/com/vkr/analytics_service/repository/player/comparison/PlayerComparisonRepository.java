package com.vkr.analytics_service.repository.player.comparison;

import com.vkr.analytics_service.entity.player.comparisons.PlayerComparison;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerComparisonRepository extends ElasticsearchRepository<PlayerComparison, String> {
}
