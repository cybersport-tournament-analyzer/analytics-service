package com.vkr.analytics_service.service.tournament;

import com.vkr.analytics_service.dto.pickban.PickBanAction;
import com.vkr.analytics_service.entity.team.TeamMetaStats;
import com.vkr.analytics_service.entity.tournament.TournamentMetaStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TournamentStatsService {

    Page<TournamentMetaStats> getAll(Pageable pageable);

    void aggregateTournamentStats(List<PickBanAction> pickBanActions, UUID tournamentId);

    List<TournamentMetaStats> getAllTournamentStats(UUID tournamentId);

    TournamentMetaStats getTournamentStatsForMap(UUID tournamentId, String map);

    void deleteAll();
}
