package com.vkr.analytics_service.repository.tournament;

import com.vkr.analytics_service.entity.tournament.TournamentMetaStats;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentStatsRepository extends ElasticsearchRepository<TournamentMetaStats, String> {

    Optional<TournamentMetaStats> findByTournamentIdAndMap(String tournamentId, String map);

    List<TournamentMetaStats> findAllByTournamentId(String tournamentId);
}
