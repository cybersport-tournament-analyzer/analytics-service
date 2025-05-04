package com.vkr.analytics_service.repository.player.map;

import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerGameStatsOnMapRepository extends ElasticsearchRepository<PlayerGameStatsOnMap, String> {
}
