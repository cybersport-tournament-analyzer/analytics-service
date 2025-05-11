package com.vkr.analytics_service.service.player.comparison;

import com.vkr.analytics_service.dto.matchmaking.KillEventDto;
import com.vkr.analytics_service.entity.player.comparisons.Duels;
import com.vkr.analytics_service.entity.player.comparisons.PlayerDuels;
import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.exception.EntityNotFoundException;
import com.vkr.analytics_service.repository.player.comparison.DuelsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DuelsServiceImpl implements DuelsService {

    private final DuelsRepository duelsRepository;

    @Override
    public void processDuels(String player1Id, String player2Id, String scope, String scopeId, List<RoundStats> allRounds, int seriesOrder) {
        String duelId = generateDuelId(scope, scopeId, seriesOrder);

        Duels duel = duelsRepository.findById(duelId)
                .orElseGet(() -> {
                    Duels newDuel = new Duels();
                    newDuel.setId(duelId);
                    newDuel.setScope(scope);
                    newDuel.setScopeId(scopeId);
                    newDuel.setDuels(new ArrayList<>());
                    return newDuel;
                });

        List<Duels> pastDuels = duelsRepository.findAllByScopeAndScopeId("match", scopeId);

        PlayerDuels playerDuel = new PlayerDuels();
        int pl1kills = 0;
        int pl2kills = 0;

        if (scope.equals("series")) {
            for(Duels pastDuel : pastDuels) {
                if (pastDuel.getDuels().get(0).getPlayer1Id().equals(player1Id)) {
                    pl1kills += pastDuel.getDuels().get(0).getPlayer1Kills();
                    pl2kills += pastDuel.getDuels().get(0).getPlayer2Kills();
                    playerDuel.setPlayer1Id(pastDuel.getDuels().get(0).getPlayer1Id());
                    playerDuel.setPlayer2Id(pastDuel.getDuels().get(0).getPlayer2Id());
                    playerDuel.setPlayer1Kills(pastDuel.getDuels().get(0).getPlayer1Kills());
                    playerDuel.setPlayer2Kills(pastDuel.getDuels().get(0).getPlayer2Kills());
                } else {
                    pl1kills += pastDuel.getDuels().get(0).getPlayer2Kills();
                    pl2kills += pastDuel.getDuels().get(0).getPlayer1Kills();
                    playerDuel.setPlayer1Kills(pastDuel.getDuels().get(0).getPlayer1Kills());
                    playerDuel.setPlayer2Kills(pastDuel.getDuels().get(0).getPlayer2Kills());
                }
            }
        } else {
            for(RoundStats roundStats : allRounds) {
                for (KillEventDto killEvent : roundStats.getKillEvents()) {
                    if (killEvent.getKillerSteamId().equals(player1Id) && killEvent.getVictimSteamId().equals(player2Id)) {
                        pl1kills++;
                    } else if (killEvent.getKillerSteamId().equals(player2Id) && killEvent.getVictimSteamId().equals(player1Id)) {
                        pl2kills++;
                    }
                }
            }
        }

        if (duel.getDuels() != null) {
            duel.getDuels().remove(0);
        }

        int totalKills = pl1kills + pl2kills;
        double player1Percent = totalKills == 0 ? 0.0 : (double) pl1kills / totalKills * 100;
        double player2Percent = totalKills == 0 ? 0.0 : (double) pl2kills / totalKills * 100;

        playerDuel.setPlayer1KillsPercent(player1Percent);
        playerDuel.setPlayer2KillsPercent(player2Percent);

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
        if (scope.equals("match"))
            duel = duelsRepository.findById(scope + "-" + scopeId + "-" + seriesOrder).orElse(null);
        else duel = duelsRepository.findById(scope + "-" + scopeId + "-X").orElse(null);
        assert duel != null;
        for (PlayerDuels playerDuel : duel.getDuels()) {
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
