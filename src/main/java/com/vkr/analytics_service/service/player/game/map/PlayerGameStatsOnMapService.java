package com.vkr.analytics_service.service.player.game.map;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;

import java.util.List;

public interface PlayerGameStatsOnMapService {

    void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players, Match match, int seriesOrder);

    PlayerGameStatsOnMap getGlobalPlayerGameStatsOnMap(String playerId, String map);
    PlayerGameStatsOnMap getTournamentPlayerGameStatsOnMap(String playerId, String tournamentId, String map);
    PlayerGameStatsOnMap getSeriesPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map);
    PlayerGameStatsOnMap getMatchPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map, int seriesOrder);
}
