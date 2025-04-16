package com.vkr.analytics_service.controller.tournament;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournament Stats Controller")
public class TournamentStatsController {
}
