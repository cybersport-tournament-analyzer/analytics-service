package com.vkr.analytics_service.repository.player.overall;

import com.vkr.analytics_service.entity.player.overall.PlayerWeaponStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerWeaponStatsRepository extends ElasticsearchRepository<PlayerWeaponStats, String> {
}
