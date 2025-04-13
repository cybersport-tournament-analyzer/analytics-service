package com.vkr.analytics_service.repository.player;

import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMetaStatsRepository extends ElasticsearchRepository<PlayerMetaStats, String> {
    List<PlayerMetaStats> findBySteamIdAndScope(String steamId, String scope);

    PlayerMetaStats findBySteamIdAndMap(String steamId, String map);
}
