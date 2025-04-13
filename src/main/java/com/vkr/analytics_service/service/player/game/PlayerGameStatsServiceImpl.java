package com.vkr.analytics_service.service.player.game;

import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.player.PlayerGameStats;
import com.vkr.analytics_service.repository.player.PlayerGameStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerGameStatsServiceImpl implements PlayerGameStatsService {

    private final PlayerGameStatsRepository playerGameStatsRepository;

    @Override
    public void aggregate(String scope, String scopeId, String map, List<PlayerStatsRaw> players) {
        for (PlayerStatsRaw raw : players) {
            String steamId = raw.getSteamId();
            String playerGameStatsId = steamId + "-" + scope + "-" + scopeId;

            PlayerGameStats playerGameStats = playerGameStatsRepository.findById(playerGameStatsId).orElse(
                    PlayerGameStats.builder()
                            .id(playerGameStatsId)
                            .steamId(steamId)
                            .scope(scope)
                            .scopeId(scopeId)
                            .map(map)
                            .side(raw.getTeam().equals("2") ? "CT" : "T")
                            .kills(0)
                            .deaths(0)
                            .assists(0)
                            .adr(0.0)
                            .hsp(0.0)
                            .clutches(0)
                            .firstKills(0)
                            .updatedAt(LocalDateTime.now())
                            .build()
            );

            playerGameStats.setKills(playerGameStats.getKills() + raw.getKills());
            playerGameStats.setDeaths(playerGameStats.getDeaths() + raw.getDeaths());
            playerGameStats.setAssists(playerGameStats.getAssists() + raw.getAssists());
            playerGameStats.setClutches(playerGameStats.getClutches() + raw.getClutchk());
            playerGameStats.setFirstKills(playerGameStats.getFirstKills() + raw.getFirstk());
            
            playerGameStats.setAdr(average(playerGameStats.getAdr(), raw.getAdr()));
            playerGameStats.setHsp(average(playerGameStats.getHsp(), raw.getHsp()));

            playerGameStats.setUpdatedAt(LocalDateTime.now());
            playerGameStatsRepository.save(playerGameStats);
        }
    }

    private double average(double a, double b) {
        if(a == 0) return b;
        if(b == 0) return a;
        return (a + b) / 2;
    }
}
