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

        // Создаем или получаем существующую дуэль
        Duels duel = duelsRepository.findById(duelId)
                .orElseGet(() -> {
                    Duels newDuel = new Duels();
                    newDuel.setId(duelId);
                    newDuel.setScope(scope);
                    newDuel.setScopeId(scopeId);
                    newDuel.setDuels(new ArrayList<>());
                    return newDuel;
                });

        // Находим существующую дуэль между игроками, если есть
        PlayerDuels existingDuel = duel.getDuels().stream()
                .filter(d -> (d.getPlayer1Id().equals(player1Id) && d.getPlayer2Id().equals(player2Id)) ||
                        (d.getPlayer1Id().equals(player2Id) && d.getPlayer2Id().equals(player1Id)))
                .findFirst()
                .orElse(null);

        int pl1kills = 0;
        int pl2kills = 0;

        // Если это серия и есть существующая дуэль - берем текущие значения
        if (scope.equals("series") && existingDuel != null) {
            // Важно сохранить оригинальное соответствие игроков из первой встречи
            if (existingDuel.getPlayer1Id().equals(player1Id)) {
                pl1kills = existingDuel.getPlayer1Kills();
                pl2kills = existingDuel.getPlayer2Kills();
            } else {
                // Если игроки поменялись местами - меняем счет тоже
                pl1kills = existingDuel.getPlayer2Kills();
                pl2kills = existingDuel.getPlayer1Kills();
            }
        }

        for(RoundStats roundStats : allRounds) {
            for (KillEventDto killEvent : roundStats.getKillEvents()) {
                if (killEvent.getKillerSteamId().equals(player1Id) && killEvent.getVictimSteamId().equals(player2Id)) {
                    pl1kills++;
                } else if (killEvent.getKillerSteamId().equals(player2Id) && killEvent.getVictimSteamId().equals(player1Id)) {
                    pl2kills++;
                }
            }
        }

        // Удаляем старую дуэль (если была)
        if (existingDuel != null) {
            duel.getDuels().remove(existingDuel);
        }

        // Создаем новую дуэль с обновленными значениями
        PlayerDuels playerDuel = new PlayerDuels();

        // Для серии сохраняем оригинальный порядок игроков из первой встречи
        if (scope.equals("series") && existingDuel != null) {
            playerDuel.setPlayer1Id(existingDuel.getPlayer1Id());
            playerDuel.setPlayer2Id(existingDuel.getPlayer2Id());

            // Корректируем счет в зависимости от порядка игроков
            if (existingDuel.getPlayer1Id().equals(player1Id)) {
                playerDuel.setPlayer1Kills(pl1kills);
                playerDuel.setPlayer2Kills(pl2kills);
            } else {
                playerDuel.setPlayer1Kills(pl2kills);
                playerDuel.setPlayer2Kills(pl1kills);
            }
        } else {
            // Для матча или новой дуэли в серии сохраняем текущий порядок
            playerDuel.setPlayer1Id(player1Id);
            playerDuel.setPlayer2Id(player2Id);
            playerDuel.setPlayer1Kills(pl1kills);
            playerDuel.setPlayer2Kills(pl2kills);
        }

        // Рассчитываем проценты
        int totalKills = pl1kills + pl2kills;
        double player1Percent = totalKills == 0 ? 0.0 : (double) pl1kills / totalKills * 100;
        double player2Percent = totalKills == 0 ? 0.0 : (double) pl2kills / totalKills * 100;

        playerDuel.setPlayer1KillsPercent(player1Percent);
        playerDuel.setPlayer2KillsPercent(player2Percent);

        // Сохраняем обновленную дуэль
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
