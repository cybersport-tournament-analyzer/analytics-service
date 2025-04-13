package com.vkr.analytics_service.repository.tournament;

import com.vkr.analytics_service.entity.tournament.TournamentStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentStatsRepository extends ElasticsearchRepository<TournamentStats, String> {

    Optional<TournamentStats> findByTournamentIdAndMap(String tournamentId, String map);
}
