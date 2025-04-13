package com.vkr.analytics_service.service.player.meta;

import com.vkr.analytics_service.dto.match.MatchMeta;
import com.vkr.analytics_service.entity.player.PlayerMetaStats;
import com.vkr.analytics_service.mapper.PlayerMetaStatsMapper;
import com.vkr.analytics_service.repository.player.PlayerMetaStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerMetaStatsServiceImpl implements PlayerMetaStatsService {

    private final PlayerMetaStatsRepository playerMetaStatsRepository;
    private final PlayerMetaStatsMapper playerMetaStatsMapper;

    @Override
    public List<PlayerMetaStats> getAllMapsPlayerMeta(String steamId, String scope) {
        return playerMetaStatsRepository.findBySteamIdAndScope(steamId,scope);
    }

    @Override
    public PlayerMetaStats getMapPlayerMeta(String steamId, String map) {
        return playerMetaStatsRepository.findBySteamIdAndMap(steamId,map);
    }

    @Override
    public void aggregatePlayerMapStats(MatchMeta meta) {
        for (String steamId : meta.getPlayerSteamIds()) {
            String docId = steamId + "-" + meta.getMap() + "-tournament-" + meta.getTournamentId();

            PlayerMetaStats doc = playerMetaStatsRepository.findById(docId).orElse(
                    PlayerMetaStats.builder()
                            .id(docId)
                            .steamId(steamId)
                            .map(meta.getMap())
                            .scope("tournament")
                            .scopeId(String.valueOf(meta.getTournamentId()))
                            .played(0)
                            .won(0)
                            .picked(0)
                            .banned(0)
                            .build()
            );

            doc.setPlayed(doc.getPlayed() + 1);
            if (meta.getWinnerSteamIds().contains(steamId)) {
                doc.setWon(doc.getWon() + 1);
            }

            if (steamId.equals(meta.getPickedBy())) {
                doc.setPicked(doc.getPicked() + 1);
            }
            if (steamId.equals(meta.getBannedBy())) {
                doc.setBanned(doc.getBanned() + 1);
            }

            doc.setWinRate(calc(doc.getWon(), doc.getPlayed()));
            doc.setPickRate(calc(doc.getPicked(), doc.getPlayed()));
            doc.setBanRate(calc(doc.getBanned(), doc.getPlayed()));

            playerMetaStatsRepository.save(doc);
        }
    }

    @Override
    public Page<PlayerMetaStats> getAllMetaStats(Pageable pageable) {
        return playerMetaStatsRepository.findAll(pageable);
    }

    private double calc(int numerator, int denominator) {
        return denominator == 0 ? 0.0 : (double) numerator / denominator;
    }
}
