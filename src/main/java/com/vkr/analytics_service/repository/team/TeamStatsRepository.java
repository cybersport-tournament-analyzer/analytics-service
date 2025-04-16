package com.vkr.analytics_service.repository.team;

import com.vkr.analytics_service.entity.team.TeamMetaStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamStatsRepository extends ElasticsearchRepository<TeamMetaStats, String> {

    List<TeamMetaStats> findAllByTournamentIdAndTeamName(String tournamentId, String teamName);
}
