package com.vkr.analytics_service.service.player.game.overall;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.dto.tournament.TeamDto;
import com.vkr.analytics_service.entity.player.overall.PlayerGameStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerGameStatsService {

    void aggregate(String scope, String scopeId, List<PlayerStatsRaw> players, Match match, int seriesOrder);

    Page<PlayerGameStats> getAllGameStats(Pageable pageable);

    void deleteAllGameStats();

    PlayerGameStats getGlobalPlayerGameStats(String playerId);
    PlayerGameStats getTournamentPlayerGameStats(String playerId, String tournamentId);
    PlayerGameStats getSeriesPlayerGameStats(String playerId, String tournamentMatchId);
    PlayerGameStats getMatchPlayerGameStats(String playerId, String tournamentMatchId, int seriesOrder);

    void initStats(TeamDto team1, TeamDto team2, String tournamentMatchId, int seriesOrder);
    void initGlobalStats(String playerId, String tournamentId);
    void initNextMatchStats(List<PlayerStatsRaw> players, String tournamentMatchId, int seriesOrder);
}
