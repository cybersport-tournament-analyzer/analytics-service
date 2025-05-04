package com.vkr.analytics_service.controller.player.stats;

import com.vkr.analytics_service.service.player.meta.map.PlayerMetaStatsOnMapService;
import com.vkr.analytics_service.service.player.meta.overall.PlayerMetaStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meta-stats/players/{playerId}")
public class PlayerMetaStatsController {

    private final PlayerMetaStatsService playerMetaStatsService;
    private final PlayerMetaStatsOnMapService playerMetaStatsOnMapService;
}
