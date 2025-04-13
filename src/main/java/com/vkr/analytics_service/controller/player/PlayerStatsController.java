package com.vkr.analytics_service.controller.player;

import com.vkr.analytics_service.entity.player.PlayerGameStats;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import com.vkr.analytics_service.service.player.game.PlayerGameStatsService;
import com.vkr.analytics_service.service.player.meta.PlayerMetaStatsService;
import com.vkr.analytics_service.service.round.RoundStatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats/players")
@RequiredArgsConstructor
@Tag(name = "Player stats controller")
public class PlayerStatsController {

    private final PlayerGameStatsService playerGameStatsService;
    private final PlayerMetaStatsService playerMetaStatsService;
    private final RoundStatsService roundStatsService;

    @GetMapping("/game")
    public Page<PlayerGameStats> getAllGameStats(Pageable pageable) {
        return playerGameStatsService.getAllGameStats(pageable);
    }

    @GetMapping("/meta")
    public Page<PlayerMetaStats> getAllMetaStats(Pageable pageable) {
        return playerMetaStatsService.getAllMetaStats(pageable);
    }

    @GetMapping("/rounds")
    public Page<RoundStats> getAllRoundsStats(Pageable pageable) {
        return roundStatsService.getAll(pageable);
    }

    @DeleteMapping("/game")
    public void deleteAllGameStats() {
        playerGameStatsService.deleteAllGameStats();
    }

    @DeleteMapping("/rounds")
    public void deleteAllRoundsStats() {
        roundStatsService.deleteAll();
    }
}
