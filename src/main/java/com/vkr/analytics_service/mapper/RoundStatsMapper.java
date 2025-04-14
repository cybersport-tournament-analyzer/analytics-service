package com.vkr.analytics_service.mapper;

import com.vkr.analytics_service.dto.matchmaking.Match;
import com.vkr.analytics_service.dto.matchmaking.RoundStatsDto;
import com.vkr.analytics_service.dto.player.PlayerStatsRaw;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.kafka.event.roundEnd.RoundEndEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                .build();
    }

    private List<PlayerStatsRaw> parsePlayers(List<Map<String, String>> rawPlayers, List<Match.Player> matchPlayers) {
        List<PlayerStatsRaw> players = new ArrayList<>();

        for (Map<String, String> map : rawPlayers) {

            Match.Player matchPlayer = matchPlayers.stream()
                    .filter(player -> player.getSteam_id_64().equals(map.get("accountid")))
                    .findFirst()
                    .orElse(null);


            players.add(PlayerStatsRaw.builder()
                    .steamId(map.get("accountid"))
                    .team(map.get("team").equals("2") ? "CT" : "T")
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
                    .bombk(toInt(map.get("bombk")))
                    .blindk(toInt(map.get("blindk")))
                    .firedmg(toInt(map.get("firedmg")))
                    .uniquek(toInt(map.get("uniquek")))
                    .dinks(toInt(map.get("dinks")))
                    .chickenk(toInt(map.get("chickenk")))
                    .score(matchPlayer != null ? matchPlayer.getStats().getScore() : 0)
                    ._2ks(matchPlayer != null ? matchPlayer.getStats().get_2ks() : 0)
                    ._3ks(matchPlayer != null ? matchPlayer.getStats().get_3ks() : 0)
                    ._4ks(matchPlayer != null ? matchPlayer.getStats().get_4ks() : 0)
                    ._5ks(matchPlayer != null ? matchPlayer.getStats().get_5ks() : 0)
                    .kills_with_headshot(matchPlayer != null ? matchPlayer.getStats().getKills_with_headshot() : 0)
                    .kills_with_pistol(matchPlayer != null ? matchPlayer.getStats().getKills_with_pistol() : 0)
                    .kills_with_sniper(matchPlayer != null ? matchPlayer.getStats().getKills_with_sniper() : 0)
                    .damage_dealt(matchPlayer != null ? matchPlayer.getStats().getDamage_dealt() : 0)
                    .entry_attempts(matchPlayer != null ? matchPlayer.getStats().getEntry_attempts() : 0)
                    .entry_successes(matchPlayer != null ? matchPlayer.getStats().getEntry_successes() : 0)
                    .flashes_thrown(matchPlayer != null ? matchPlayer.getStats().getFlashes_thrown() : 0)
                    .flashes_successful(matchPlayer != null ? matchPlayer.getStats().getFlashes_successful() : 0)
                    .flashes_enemies_blinded(matchPlayer != null ? matchPlayer.getStats().getFlashes_enemies_blinded() : 0)
                    .utility_thrown(matchPlayer != null ? matchPlayer.getStats().getUtility_thrown() : 0)
                    .utility_damage(matchPlayer != null ? matchPlayer.getStats().getUtility_damage() : 0)
                    ._1vX_attempts(matchPlayer != null ? matchPlayer.getStats().get_1vX_attempts() : 0)
                    ._1vX_wins(matchPlayer != null ? matchPlayer.getStats().get_1vX_wins() : 0)
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
