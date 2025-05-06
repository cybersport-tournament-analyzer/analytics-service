package com.vkr.analytics_service.repository.player.overall;

import com.vkr.analytics_service.entity.player.overall.PlayerWeaponStats;
import io.micrometer.core.instrument.config.validate.Validated;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerWeaponStatsRepository extends ElasticsearchRepository<PlayerWeaponStats, String> {
    Validated<Object> findAllById(String id);

    List<PlayerWeaponStats> findAllBySteamIdAndScopeId(String steamId, String scopeId);

    List<PlayerWeaponStats> findAllBySteamIdAndScopeAndScopeId(String steamId, String scope, String scopeId);

    List<PlayerWeaponStats> findAllBySteamIdAndScopeAndScopeIdAndSeriesOrder(String steamId, String scope, String scopeId, int seriesOrder);
}
