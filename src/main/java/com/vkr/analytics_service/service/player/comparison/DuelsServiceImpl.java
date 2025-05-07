package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.entity.player.comparisons.Duels;
import com.vkr.analytics_service.entity.player.comparisons.PlayerDuels;
import com.vkr.analytics_service.exception.EntityNotFoundException;
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
        String duelId = generateDuelId(scope, scopeId, seriesOrder);

        Duels duel = duelsRepository.findById(duelId)
                .orElseThrow(() -> new EntityNotFoundException("Duels not found for ID: " + duelId));

        int pl1kills = 0;
        int pl2kills = 0;

        for (KillEventDto killEvent : killEvents) {
            if (killEvent.getKillerSteamId().equals(player1Id) && killEvent.getVictimSteamId().equals(player2Id)) {
                pl1kills++;
            } else if (killEvent.getKillerSteamId().equals(player2Id) && killEvent.getVictimSteamId().equals(player1Id)) {
                pl2kills++;
            }
        }

        int totalKills = pl1kills + pl2kills;
        double player1Percent = totalKills == 0 ? 0.0 : (double) pl1kills / totalKills;
        double player2Percent = totalKills == 0 ? 0.0 : (double) pl2kills / totalKills;

        PlayerDuels playerDuel = PlayerDuels.builder()
                .player1Id(player1Id)
                .player2Id(player2Id)
                .player1Kills(pl1kills)
                .player2Kills(pl2kills)
                .player1KillsPercent(player1Percent)
                .player2KillsPercent(player2Percent)
                .build();

        duel.getDuels().add(playerDuel);
        duelsRepository.save(duel);
    }

    private String generateDuelId(String scope, String scopeId, int seriesOrder) {
        return scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X");
    }

    @Override
    public void createDuel(String scope, String scopeId, int seriesOrder) {
        Duels duel = new Duels(scope + "-" + scopeId + (scope.equals("match") ? "-" + seriesOrder : "-X"), scope, scopeId, new ArrayList<>());
        duelsRepository.save(duel);
    }

    @Override
    public PlayerDuels findByPlayers(String player1Id, String player2Id, String scope, String scopeId, int seriesOrder) {
        Duels duel;
        if(scope.equals("match")) duel = duelsRepository.findById(scope + "-" + scopeId + "-" + seriesOrder).orElse(null);
        else duel = duelsRepository.findById(scope + "-" + scopeId + "-X").orElse(null);
        assert duel != null;
        for(PlayerDuels playerDuel : duel.getDuels()) {
            if ((playerDuel.getPlayer1Id().equals(player1Id) && playerDuel.getPlayer2Id().equals(player2Id)) ||
                    (playerDuel.getPlayer1Id().equals(player2Id) && playerDuel.getPlayer2Id().equals(player1Id))) {
                return playerDuel;
            }
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
