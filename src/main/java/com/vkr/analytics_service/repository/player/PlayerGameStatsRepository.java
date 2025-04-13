package com.vkr.analytics_service.repository.player;

import com.vkr.analytics_service.entity.player.PlayerGameStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerGameStatsRepository extends ElasticsearchRepository<PlayerGameStats, String> {

    List<PlayerGameStats> findBySteamIdAndScope(String steamId, String scope);
    List<PlayerGameStats> findBySteamIdAndScopeAndScopeId(String steamId, String scope, String scopeId);
}
