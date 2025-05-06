package com.vkr.analytics_service.service.team;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.pickban.PickBanAction;
import com.vkr.analytics_service.entity.team.TeamMetaStats;

import java.util.List;
import java.util.UUID;

public interface TeamStatsService {

    void aggregateTeamMetaStats(Match match, UUID tournamentId, String teamName);
    void aggregateTeamPickBanStats(List<PickBanAction> actions, UUID tournamentId, String teamName, String teamPos);

    List<TeamMetaStats> getAllTeamMetaStats(UUID tournamentId, String teamName);

    TeamMetaStats getTeamMetaStatsForMap(UUID tournamentId, String teamName, String map);

    void deleteAll();
}
