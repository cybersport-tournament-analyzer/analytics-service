package com.vkr.analytics_service.service.team;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.pickban.PickBanAction;
import com.vkr.analytics_service.entity.team.TeamMetaStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TeamStatsService {

    void aggregateTeamMetaStats(Match match, UUID tournamentId, String teamName);
    void aggregateTeamPickBanStats(List<PickBanAction> actions, UUID tournamentId, String teamName, String teamPos);

    Page<TeamMetaStats> getAllTeamMetaStats(UUID tournamentId, String teamName, Pageable pageable);

    TeamMetaStats getTeamMetaStatsForMap(UUID tournamentId, String teamName, String map);

    void deleteAll();
}
