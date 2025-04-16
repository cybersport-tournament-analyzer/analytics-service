package com.vkr.analytics_service.controller.match;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats/series")
@RequiredArgsConstructor
@Tag(name = "Match Series stats Controller")
public class SeriesStatsController {
}
