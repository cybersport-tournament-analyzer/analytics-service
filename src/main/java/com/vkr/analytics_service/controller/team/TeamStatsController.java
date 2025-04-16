package com.vkr.analytics_service.controller.team;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats/team")
@RequiredArgsConstructor
@Tag(name = "Team stats controller")
public class TeamStatsController {
}
