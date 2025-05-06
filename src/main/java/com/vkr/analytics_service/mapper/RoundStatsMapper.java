package com.vkr.analytics_service.mapper;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.matchmaking.RoundStatsDto;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RoundStatsMapper {


    public RoundStats toDocument(RoundEndEvent event) {
        RoundStatsDto stats = event.getRoundStats();

        return RoundStats.builder()
                .id(UUID.randomUUID())
                .tournamentId(event.getTournamentId())
                .matchId(event.getTournamentMatchId())
                .roundNumber(stats.getRoundNumber())
                .map(stats.getMap())
                .players(parsePlayers(stats.getPlayers(), event.getMatch().getPlayers()))
                .killEvents(event.getKillEvents())
                .roundEndReason(event.getRoundEndReason())
                .seriesOrder(event.getSeriesOrder())
                .usefulRound(new HashMap<>())
                .build();
    }

    private List<PlayerStatsRaw> parsePlayers(List<Map<String, String>> rawPlayers, List<Match.Player> matchPlayers) {
        List<PlayerStatsRaw> players = new ArrayList<>();

        for (Map<String, String> map : rawPlayers) {

            Match.Player matchPlayer = matchPlayers.stream()
                    .filter(player -> player.getSteam_id_64().equals(map.get("accountid")))
                    .findFirst()
                    .orElse(null);

            if(matchPlayer.getNickname_override().equals("observer")) continue;

            players.add(PlayerStatsRaw.builder()
                    .steamId(map.get("accountid"))
                    .kills(matchPlayer.getStats().getKills())
                    .deaths(matchPlayer.getStats().getDeaths())
                    .assists(matchPlayer.getStats().getAssists())
                    .firstk(toInt(map.get("firstk")))
                    .bombk(toInt(map.get("bombk")))
                    .blindk(toInt(map.get("blindk")))
                    .firedmg(toInt(map.get("firedmg")))
                    .uniquek(toInt(map.get("uniquek")))
                    .dinks(toInt(map.get("dinks")))
                    .chickenk(toInt(map.get("chickenk")))
                    ._2ks(matchPlayer.getStats().get_2ks())
                    ._3ks(matchPlayer.getStats().get_3ks())
                    ._4ks(matchPlayer.getStats().get_4ks())
                    ._5ks(matchPlayer.getStats().get_5ks())
                    .killsWithHeadshot(matchPlayer.getStats().getKills_with_headshot())
                    .killsWithPistol(matchPlayer.getStats().getKills_with_pistol())
                    .killsWithSniper(matchPlayer.getStats().getKills_with_sniper())
                    .damageDealt(matchPlayer.getStats().getDamage_dealt())
                    .entryAttempts(matchPlayer.getStats().getEntry_attempts())
                    .entrySuccesses(matchPlayer.getStats().getEntry_successes())
                    .flashesThrown(matchPlayer.getStats().getFlashes_thrown())
                    .flashesSuccessful(matchPlayer.getStats().getFlashes_successful())
                    .flashesEnemiesBlinded(matchPlayer.getStats().getFlashes_enemies_blinded())
                    .utilityThrown(matchPlayer.getStats().getUtility_thrown())
                    .utilityDamage(matchPlayer.getStats().getUtility_damage())
                    .oneVXAttempts(matchPlayer.getStats().get_1vX_attempts())
                    .oneVXWins(matchPlayer.getStats().get_1vX_wins())
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
}
