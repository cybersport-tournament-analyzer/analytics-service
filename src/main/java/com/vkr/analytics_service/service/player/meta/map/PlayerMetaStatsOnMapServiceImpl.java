package com.vkr.analytics_service.service.player.meta.map;

import com.vkr.analytics_service.entity.player.map.PlayerMetaStatsOnMap;
import com.vkr.analytics_service.repository.player.overall.PlayerMetaStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerMetaStatsOnMapServiceImpl implements PlayerMetaStatsOnMapService {

    private final PlayerMetaStatsRepository playerMetaStatsRepository;

    @Override
    public PlayerMetaStatsOnMap getGlobalPlayerMetaStatsOnMap(String playerId, String map) {
        return null;
    }

    @Override
    public PlayerMetaStatsOnMap getTournamentPlayerMetaStatsOnMap(String playerId, String tournamentId, String map) {
        return null;
    }
}
