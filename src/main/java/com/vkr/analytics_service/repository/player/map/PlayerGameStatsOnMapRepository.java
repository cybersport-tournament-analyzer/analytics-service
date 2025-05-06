package com.vkr.analytics_service.repository.player.map;

import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerGameStatsOnMapRepository extends ElasticsearchRepository<PlayerGameStatsOnMap, String> {
    PlayerGameStatsOnMap findBySteamIdAndMap(String steamId, String map);

    PlayerGameStatsOnMap findBySteamIdAndMapAndScope(String steamId, String map, String scope);

    PlayerGameStatsOnMap findBySteamIdAndMapAndScopeId(String steamId, String map, String scopeId);

    PlayerGameStatsOnMap findBySteamIdAndMapAndScopeIdAndSeriesOrder(String steamId, String map, String scopeId, int seriesOrder);
}
