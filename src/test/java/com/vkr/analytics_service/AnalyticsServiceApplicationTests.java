package com.vkr.analytics_service;

import com.vkr.analytics_service.entity.Match;
import com.vkr.analytics_service.entity.MatchSeries;
import com.vkr.analytics_service.entity.PlayerAverages;
import com.vkr.analytics_service.entity.PlayerStats;
import com.vkr.analytics_service.services.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnalyticsServiceApplicationTests {


	private MatchService matchService;

	@BeforeEach
	void setUp() {
		matchService = new MatchService();
	}

	@Test
	void getMatchById_withInvalidId_shouldThrowException() {
		// Act & Assert
		Exception exception = assertThrows(RuntimeException.class, () ->
				matchService.getMatchById(999L)
		);
		assertEquals("Match not found", exception.getMessage());
	}

	@Test
	void getAllSeries_shouldReturnAllSeries() {
		// Act
		List<MatchSeries> seriesList = matchService.getAllSeries();

		// Assert
		assertNotNull(seriesList);
		assertEquals(1, seriesList.size());
	}

	@Test
	void getSeriesById_withValidId_shouldReturnSeries() {
		// Act
		MatchSeries series = matchService.getSeriesById(1L);

		// Assert
		assertNotNull(series);
		assertEquals(1L, series.getId());
		assertEquals("2-0", series.getScore());
		assertEquals(2, series.getMatches().size());
	}
	@Test
	void getAllMatches_shouldReturnAllMatches() {
		// Act
		List<Match> matches = matchService.getAllMatches();
		System.out.println(matches);

		// Assert
		assertNotNull(matches);
		assertEquals(2, matches.size());
	}

	@Test
	void getMatchById_withValidId_shouldReturnMatch() {
		// Act
		Match match = matchService.getMatchById(1L);

		// Assert
		assertNotNull(match);
		assertEquals(1L, match.getId());
		assertEquals("Dust II", match.getMapName());
		assertEquals("16-14", match.getScore());
	}



	@Test
	void getSeriesById_withInvalidId_shouldThrowException() {
		// Act & Assert
		Exception exception = assertThrows(RuntimeException.class, () ->
				matchService.getSeriesById(999L)
		);
		assertEquals("Series not found", exception.getMessage());
	}

	@Test
	void calculateSeriesAverages_shouldCalculateCorrectAverages() {
		// Arrange
		MatchSeries series = matchService.getSeriesById(1L);

		// Assert
		List<PlayerAverages> averages = series.getPlayerAverages();
		assertNotNull(averages);
		assertEquals(2, averages.size());

		// Find averages for player yofujitsu
		PlayerAverages yofujitsuAvg = findPlayerAverageById(averages, "b160f59e-1b3e-431c-8e07-be642a745c85");
		assertNotNull(yofujitsuAvg);
		assertEquals(14.5, yofujitsuAvg.getAvgKills(), 0.001);
		assertEquals(9.5, yofujitsuAvg.getAvgDeaths(), 0.001);
		assertEquals(0.0, yofujitsuAvg.getAvgAssists(), 0.001);
		assertEquals(1418.75, yofujitsuAvg.getAvgDamage(), 0.001);

		// Find averages for player Ulquiorra
		PlayerAverages ulquiorraAvg = findPlayerAverageById(averages, "9f088a5b-32c1-40ab-bc5f-2a4c7e689cdb");
		assertNotNull(ulquiorraAvg);
		assertEquals(9.5, ulquiorraAvg.getAvgKills(), 0.001);
		assertEquals(14.5, ulquiorraAvg.getAvgDeaths(), 0.001);
		assertEquals(0.0, ulquiorraAvg.getAvgAssists(), 0.001);
		assertEquals(1375.0, ulquiorraAvg.getAvgDamage(), 0.001);
	}

	@Test
	void initializeDemoData_shouldCreateTwoMatchesAndOneSeries() {
		// Already called in the constructor, just verify the data
		List<Match> matches = matchService.getAllMatches();
		List<MatchSeries> series = matchService.getAllSeries();

		assertEquals(2, matches.size());
		assertEquals(1, series.size());

		// Verify first match details
		Match match1 = matches.get(0);
		assertEquals(1L, match1.getId());
		assertEquals("Dust II", match1.getMapName());
		assertEquals("16-14", match1.getScore());
		assertEquals(2, match1.getPlayersStats().size());

		// Verify player stats in first match
		PlayerStats yofujitsuStats = findPlayerStatsById(match1.getPlayersStats(),
				"b160f59e-1b3e-431c-8e07-be642a745c85");
		assertEquals("yofujitsu", yofujitsuStats.getNickname());
		assertEquals(16, yofujitsuStats.getKills());
		assertEquals(14, yofujitsuStats.getDeaths());
		assertEquals(1587.5, yofujitsuStats.getDamage(), 0.001);

		// Verify series
		MatchSeries firstSeries = series.get(0);
		assertEquals("2-0", firstSeries.getScore());
		assertEquals(2, firstSeries.getMatches().size());
		assertEquals(2, firstSeries.getPlayerAverages().size());
	}

	// Helper method to find player averages by ID
	private PlayerAverages findPlayerAverageById(List<PlayerAverages> averages, String playerId) {
		return averages.stream()
				.filter(avg -> avg.getPlayerId().equals(playerId))
				.findFirst()
				.orElse(null);
	}

	// Helper method to find player stats by ID
	private PlayerStats findPlayerStatsById(List<PlayerStats> stats, String playerId) {
		return stats.stream()
				.filter(s -> s.getPlayerId().equals(playerId))
				.findFirst()
				.orElse(null);
	}


}
