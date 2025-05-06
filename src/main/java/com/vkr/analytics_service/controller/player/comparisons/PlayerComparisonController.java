package com.vkr.analytics_service.controller.player.comparisons;


import com.vkr.analytics_service.entity.player.comparisons.PlayerComparison;
import com.vkr.analytics_service.service.player.comparison.PlayerComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comparison")
public class PlayerComparisonController {

    private final PlayerComparisonService playerComparisonService;

    @GetMapping("/{seriesId}")
    public PlayerComparison getPlayerComparison(@PathVariable("seriesId") String seriesId) {
        return playerComparisonService.getPlayerComparisonSeries(seriesId);
    }

    @GetMapping("/{seriesId}/{seriesOrder}")
    public PlayerComparison getPlayerComparison(@PathVariable("seriesId") String seriesId, @PathVariable("seriesOrder") int seriesOrder) {
        return playerComparisonService.getPlayerComparisonMatch(seriesId, seriesOrder);
    }

    @GetMapping("/{seriesId}/{player1Id}/{player2Id}")
    public PlayerComparison.PlayerComparison1v1 get1v1Comparison(@PathVariable("seriesId") String seriesId,
                                                                 @PathVariable("player1Id") String player1Id,
                                                                 @PathVariable("player2Id") String player2Id) {
        return playerComparisonService.getPlayerComparison1v1Series(seriesId, player1Id, player2Id);
    }

    @GetMapping("/{seriesId}/{player1Id}/{player2Id}/{seriesOrder}")
    public PlayerComparison.PlayerComparison1v1 get1v1Comparison(@PathVariable("seriesId") String seriesId,
                                                                 @PathVariable("player1Id") String player1Id,
                                                                 @PathVariable("player2Id") String player2Id,
                                                                 @PathVariable("seriesOrder") int seriesOrder) {
        return playerComparisonService.getPlayerComparison1v1Match(seriesId, player1Id, player2Id, seriesOrder);
    }
}
