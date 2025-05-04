package com.vkr.analytics_service.service.player.game.map;

import com.vkr.analytics_service.entity.player.map.PlayerGameStatsOnMap;
import com.vkr.analytics_service.repository.player.map.PlayerGameStatsOnMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerGameStatsOnMapServiceImpl implements PlayerGameStatsOnMapService {

    private final PlayerGameStatsOnMapRepository playerGameStatsOnMapRepository;

    @Override
    public PlayerGameStatsOnMap getGlobalPlayerGameStatsOnMap(String playerId, String map) {
        return null;
    }

    @Override
    public PlayerGameStatsOnMap getTournamentPlayerGameStatsOnMap(String playerId, String tournamentId, String map) {
        return null;
    }

    @Override
    public PlayerGameStatsOnMap getSeriesPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map) {
        return null;
    }

    @Override
    public PlayerGameStatsOnMap getMatchPlayerGameStatsOnMap(String playerId, String tournamentMatchId, String map) {
        return null;
    }
}
