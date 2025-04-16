package com.vkr.analytics_service.repository.player;

import com.vkr.analytics_service.entity.player.PlayerWeaponStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerWeaponStatsRepository extends ElasticsearchRepository<PlayerWeaponStats, String> {
}
