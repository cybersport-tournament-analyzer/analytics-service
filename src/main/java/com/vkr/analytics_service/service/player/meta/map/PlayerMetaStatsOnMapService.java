package com.vkr.analytics_service.service.player.meta.map;

import com.vkr.analytics_service.entity.player.map.PlayerMetaStatsOnMap;

public interface PlayerMetaStatsOnMapService {

    PlayerMetaStatsOnMap getGlobalPlayerMetaStatsOnMap(String playerId, String map);
    PlayerMetaStatsOnMap getTournamentPlayerMetaStatsOnMap(String playerId, String tournamentId, String map);
}
