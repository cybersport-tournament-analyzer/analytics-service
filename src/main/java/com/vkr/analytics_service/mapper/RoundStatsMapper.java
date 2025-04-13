package com.vkr.analytics_service.mapper;

import com.vkr.analytics_service.dto.matchmaking.RoundStatsDto;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoundStatsMapper {


    public RoundStats toDocument(RoundEndEvent event) {
        RoundStatsDto stats = event.getRoundStats();

        return RoundStats.builder()
                .tournamentId(event.getTournamentId())
                .matchId(event.getTournamentMatchId())
                .roundNumber(stats.getRoundNumber())
                .map(stats.getMap())
                .players(parsePlayers(stats.getPlayers()))
                .killEvents(event.getKillEvents())
                .roundEndReason(event.getRoundEndReason())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private List<PlayerStatsRaw> parsePlayers(List<Map<String, String>> rawPlayers) {
        List<PlayerStatsRaw> players = new ArrayList<>();

        for (Map<String, String> map : rawPlayers) {
            players.add(PlayerStatsRaw.builder()
                    .steamId(map.get("accountid"))
                    .team(map.get("team"))
                    .kills(toInt(map.get("kills")))
                    .deaths(toInt(map.get("deaths")))
                    .assists(toInt(map.get("assists")))
                    .damage(toInt(map.get("dmg")))
                    .adr(toDouble(map.get("adr")))
                    .hsp(toDouble(map.get("hsp")))
                    .kdr(toDouble(map.get("kdr")))
                    .mvps(toInt(map.get("mvp")))
                    .clutchk(toInt(map.get("clutchk")))
                    .firstk(toInt(map.get("firstk")))
                    .threeK(toInt(map.get("3k")))
                    .fourK(toInt(map.get("4k")))
                    .fiveK(toInt(map.get("5k")))
                    .build());
        }

        return players;
    }

    private static int toInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static double toDouble(String val) {
        try {
            return Double.parseDouble(val.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
