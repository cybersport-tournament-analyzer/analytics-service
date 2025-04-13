package com.vkr.analytics_service.controller.player;

import com.vkr.analytics_service.entity.player.PlayerGameStats;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import com.vkr.analytics_service.service.player.game.PlayerGameStatsService;
import com.vkr.analytics_service.service.player.meta.PlayerMetaStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats/players")
@RequiredArgsConstructor
public class PlayerStatsController {

    private final PlayerGameStatsService playerGameStatsService;
    private final PlayerMetaStatsService playerMetaStatsService;

    @GetMapping("/game")
    public Page<PlayerGameStats> getAllGameStats(Pageable pageable) {
        return playerGameStatsService.getAllGameStats(pageable);
    }

    @GetMapping("/meta")
    public Page<PlayerMetaStats> getAllMetaStats(Pageable pageable) {
        return playerMetaStatsService.getAllMetaStats(pageable);
    }
}
