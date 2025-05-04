package com.vkr.analytics_service.controller.player.comparisons;


import com.vkr.analytics_service.service.player.comparison.PlayerComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comparison")
public class PlayerComparisonController {

    private final PlayerComparisonService playerComparisonService;
}
