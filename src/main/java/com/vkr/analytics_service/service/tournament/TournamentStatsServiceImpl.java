package com.vkr.analytics_service.service.tournament;

import com.vkr.analytics_service.dto.pickban.Action;
import com.vkr.analytics_service.dto.pickban.PickBanAction;
import com.vkr.analytics_service.entity.tournament.TournamentMetaStats;
import com.vkr.analytics_service.repository.tournament.TournamentStatsRepository;
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
public class TournamentStatsServiceImpl implements TournamentStatsService {

    private final TournamentStatsRepository tournamentStatsRepository;

    @Override
    public Page<TournamentMetaStats> getAll(Pageable pageable) {
        return tournamentStatsRepository.findAll(pageable);
    }

    @Override
    public void aggregateTournamentStats(List<PickBanAction> pickBanActions, UUID tournamentId) {
        List<TournamentMetaStats> allTournamentStats = tournamentStatsRepository.findAllByTournamentId(String.valueOf(tournamentId));
        int totalMatches = allTournamentStats.stream().mapToInt(TournamentMetaStats::getMatchesPlayed).sum();

        for (PickBanAction pickBanAction : pickBanActions) {
            String map = pickBanAction.getMapOrSide();
            String statId = tournamentId + "-" + map;
            if (pickBanAction.getAction() == Action.PICK || pickBanAction.getAction() == Action.PICK_SIDE) {
                TournamentMetaStats stats = tournamentStatsRepository.findById(statId).orElseGet(() -> TournamentMetaStats.builder()
                        .id(statId)
                        .tournamentId(String.valueOf(tournamentId))
                        .map(map)
                        .build());
                stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);
                stats.setPickRate((double) stats.getMatchesPlayed() / (totalMatches != 0 ? totalMatches : 1));
                tournamentStatsRepository.save(stats);
            } else if (pickBanAction.getAction() == Action.BAN) {
                TournamentMetaStats stats = tournamentStatsRepository.findById(statId).orElseGet(() -> TournamentMetaStats.builder()
                        .id(statId)
                        .tournamentId(String.valueOf(tournamentId))
                        .map(map)
                        .build());
                stats.setBannedCount(stats.getBannedCount() + 1);
                stats.setBanRate((double) stats.getBannedCount() / (totalMatches != 0 ? totalMatches : 1));
                tournamentStatsRepository.save(stats);
            }
        }
    }

    @Override
    public List<TournamentMetaStats> getAllTournamentStats(UUID tournamentId) {
        return tournamentStatsRepository.findAllByTournamentId(String.valueOf(tournamentId));
    }

    @Override
    public TournamentMetaStats getTournamentStatsForMap(UUID tournamentId, String map) {
        return tournamentStatsRepository.findByTournamentIdAndMap(String.valueOf(tournamentId), map).orElse(null);
    }

    @Override
    public void deleteAll() {
        tournamentStatsRepository.deleteAll();
    }

}
