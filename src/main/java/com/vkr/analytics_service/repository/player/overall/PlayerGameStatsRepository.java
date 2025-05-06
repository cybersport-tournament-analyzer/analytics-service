package com.vkr.analytics_service.repository.player.overall;

import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerGameStatsRepository extends ElasticsearchRepository<PlayerGameStats, String> {

    PlayerGameStats findBySteamIdAndScope(String steamId, String scope);

    PlayerGameStats findBySteamIdAndScopeAndScopeId(String steamId, String scope, String scopeId);

    PlayerGameStats findFirstBySteamIdAndScopeAndScopeId(String steamId, String scope, String scopeId);

    PlayerGameStats findBySteamIdAndScopeAndScopeIdAndSeriesOrder(String steamId, String scope, String scopeId, int seriesOrder);
}
