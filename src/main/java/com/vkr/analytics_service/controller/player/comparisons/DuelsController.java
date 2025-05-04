package com.vkr.analytics_service.controller.player.comparisons;

import com.vkr.analytics_service.service.player.comparison.DuelsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/duels")
public class DuelsController {

    private final DuelsService duelsService;
}
