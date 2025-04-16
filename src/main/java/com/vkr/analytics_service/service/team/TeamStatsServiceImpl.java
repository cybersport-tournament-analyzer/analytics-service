package com.vkr.analytics_service.service.team;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.pickban.Action;
import com.vkr.analytics_service.dto.pickban.PickBanAction;
import com.vkr.analytics_service.entity.team.TeamMetaStats;
import com.vkr.analytics_service.repository.team.TeamStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamStatsServiceImpl implements TeamStatsService {

    private final TeamStatsRepository teamStatsRepository;

    @Override
    public void aggregateTeamMetaStats(Match match, UUID tournamentId, String teamName) {

        String map = match.getSettings().getMap();
        String statId = teamName + "-" + map + "-" + tournamentId;

        TeamMetaStats stats = teamStatsRepository.findById(statId).orElseGet(() -> TeamMetaStats.builder()
                .id(statId)
                .teamName(teamName)
                .tournamentId(String.valueOf(tournamentId))
                .map(map)
                .build());

        stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);
        if (match.getTeam1().getName().equals(teamName)) {
            if (match.getTeam1().getStats().getScore() > match.getTeam2().getStats().getScore()) {
                stats.setMatchesWon(stats.getMatchesWon() + 1);
            } else stats.setMatchesLost(stats.getMatchesLost() + 1);
        } else {
            if (match.getTeam2().getStats().getScore() > match.getTeam1().getStats().getScore()) {
                stats.setMatchesWon(stats.getMatchesWon() + 1);
            } else stats.setMatchesLost(stats.getMatchesLost() + 1);
        }
        stats.setWinRate((double) stats.getMatchesWon() / stats.getMatchesPlayed());
        teamStatsRepository.save(stats);
    }

    @Override
    public void aggregateTeamPickBanStats(List<PickBanAction> actions, UUID tournamentId, String teamName, String teamPos) {
        List<TeamMetaStats> allTeamStats = teamStatsRepository.findAllByTournamentIdAndTeamName(String.valueOf(tournamentId), String.valueOf(teamName));
        int totalMatches = allTeamStats.stream().mapToInt(TeamMetaStats::getMatchesPlayed).sum();

        for (PickBanAction pickBanAction : actions) {
            String map = pickBanAction.getMapOrSide();
            String statId = teamName + "-" + map + "-" + tournamentId;
            if (pickBanAction.getAction() == Action.PICK && pickBanAction.getTeam().equals(teamPos)) {
                TeamMetaStats stats = teamStatsRepository.findById(statId).orElseGet(() -> TeamMetaStats.builder()
                        .id(statId)
                        .teamName(teamName)
                        .tournamentId(String.valueOf(tournamentId))
                        .map(map)
                        .build());
                stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);
                stats.setPickRate((double) stats.getMatchesPlayed() / totalMatches);
                teamStatsRepository.save(stats);
            } else if (pickBanAction.getAction() == Action.BAN && pickBanAction.getTeam().equals(teamPos)) {
                TeamMetaStats stats = teamStatsRepository.findById(statId).orElseGet(() -> TeamMetaStats.builder()
                        .id(statId)
                        .teamName(teamName)
                        .tournamentId(String.valueOf(tournamentId))
                        .map(map)
                        .build());
                stats.setBannedCount(stats.getBannedCount() + 1);
                stats.setBanRate((double) stats.getBannedCount() / totalMatches);
                teamStatsRepository.save(stats);
            }
        }
    }

    @Override
    public Page<TeamMetaStats> getAllTeamMetaStats(UUID tournamentId, String teamName, Pageable pageable) {
        return null;
    }

    @Override
    public TeamMetaStats getTeamMetaStatsForMap(UUID tournamentId, String teamName, String map) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
