package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.entity.player.comparisons.Duels;
import com.vkr.analytics_service.entity.player.comparisons.PlayerDuels;
import com.vkr.analytics_service.repository.player.comparison.DuelsRepository;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DuelsServiceImpl implements DuelsService {

    private final DuelsRepository duelsRepository;

    @Override
    public void processDuels(String player1Id, String player2Id, String scope, String scopeId, List<KillEventDto> killEvents, int seriesOrder) {
        Duels duel = duelsRepository.findById(scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X")).orElse(null);
        int pl1kills = 0;
        int pl2kills = 0;
        for (KillEventDto killEvent : killEvents) {
            if (killEvent.getKillerSteamId().equals(player1Id) && killEvent.getVictimSteamId().equals(player2Id))
                pl1kills++;
            else if (killEvent.getKillerSteamId().equals(player2Id) && killEvent.getVictimSteamId().equals(player1Id))
                pl2kills++;
        }
        PlayerDuels playerDuel = PlayerDuels.builder()
                .player1Kills(pl1kills)
                .player2Kills(pl2kills)
                .player1Id(player1Id)
                .player2Id(player2Id)
                .player1KillsPercent((double) pl1kills / (pl1kills + pl2kills))
                .player2KillsPercent((double) pl2kills / (pl1kills + pl2kills))
                .build();

        assert duel != null;
        duel.getDuels().add(playerDuel);
    }

    @Override
    public void createDuel(String scope, String scopeId, int seriesOrder) {
        Duels duel = new Duels(scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X"), scope, scopeId, new ArrayList<>());
        duelsRepository.save(duel);
    }

    @Override
    public PlayerDuels findByPlayers(String player1Id, String player2Id, String scope, String scopeId) {
        Duels duel = duelsRepository.findById(scope + scopeId).orElse(null);
        assert duel != null;
        for(PlayerDuels playerDuel : duel.getDuels()) {
            if(playerDuel.getPlayer1Id().equals(player1Id) && playerDuel.getPlayer2Id().equals(player2Id))
                return playerDuel;
        }
        return null;
    }

    @Override
    public Duels getMatchDuels(String seriesId, int seriesOrder) {
        return duelsRepository.findById("match-" + seriesId + "-" + seriesOrder).orElse(null);
    }

    @Override
    public Duels getSeriesDuels(String seriesId) {
        return duelsRepository.findById("series-" + seriesId + "-X").orElse(null);
    }
}
