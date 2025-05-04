package com.vkr.analytics_service.service.player.game.map;

import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;

public interface PlayerGameStatsOnMapService {

    PlayerGameStatsOnMap getGlobalPlayerGameStatsOnMap(String playerId, String map);
    PlayerGameStatsOnMap getTournamentPlayerGameStatsOnMap(String playerId, String tournamentId, String map);
    PlayerGameStatsOnMap getSeriesPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map);
    PlayerGameStatsOnMap getMatchPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map);
}
