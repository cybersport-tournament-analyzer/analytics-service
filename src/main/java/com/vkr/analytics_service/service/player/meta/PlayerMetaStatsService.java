package com.vkr.analytics_service.service.player.meta;

import com.vkr.analytics_service.dto.match.MatchMeta;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;

import java.util.List;


public interface PlayerMetaStatsService {

    List<PlayerMetaStats> getAllMapsPlayerMeta(String steamId, String scope);

    PlayerMetaStats getMapPlayerMeta(String steamId, String map);

    void aggregatePlayerMapStats(MatchMeta meta);
}
