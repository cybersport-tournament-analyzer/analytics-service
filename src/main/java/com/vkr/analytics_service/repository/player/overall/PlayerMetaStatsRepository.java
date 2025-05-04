package com.vkr.analytics_service.repository.player.overall;

import com.vkr.analytics_service.entity.player.overall.PlayerMetaStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMetaStatsRepository extends ElasticsearchRepository<PlayerMetaStats, String> {
    List<PlayerMetaStats> findBySteamIdAndScope(String steamId, String scope);

    PlayerMetaStats findBySteamId(String steamId);
}
