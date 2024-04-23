package eu.romanhan.BettingApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class BettingApplicationTest {

	@Test
	void testReadMatchData()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		BettingApplication.readMatchData();

		Field field = BettingApplication.class.getDeclaredField("matches");
		field.setAccessible(true);

		List<Match> matches = (List<Match>) field.get(null);

		assertEquals(13, matches.size(), "Number of matches is wrong");
		assertEquals(UUID.fromString("abae2255-4255-4304-8589-737cdff61640"), matches.get(0).getMatchId(),
				"First match id is invalid");
		assertEquals(UUID.fromString("f6ffad45-a63f-448b-9a14-ec8a10c77a45"),
				matches.get(matches.size() - 1).getMatchId(), "Last match id is invalid");

		assertEquals(1.45, matches.get(0).getRateA(), "Rate A is invalid");
		assertEquals("A", matches.get(0).getResult(), "Match side is invalid");
	}

	@Test
	void testReadPlayerData()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		BettingApplication.readPlayerData();

		Field field = BettingApplication.class.getDeclaredField("players");
		field.setAccessible(true);

		List<Player> players = (List<Player>) field.get(null);
		assertEquals(2, players.size(), "Players size is wrong");
		assertEquals(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"), players.get(0).getPlayerId(),
				"Players UUID is wrong");
	}

	@Test
	void testReadPlayerActions() {
		String[] testData = { "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4", "DEPOSIT", null, "500" };
		String[] testResult = BettingApplication.readPlayerActions(testData);

		assertEquals("DEPOSIT", testResult[0]);
		assertEquals("500", testResult[1]);

		String[] testBet = { "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4", "BET", "abae2255-4255-4304-8589-737cdff61640",
				"100", "A" };
		String[] testResult2 = BettingApplication.readPlayerActions(testBet);
		assertEquals("BET", testResult2[0]);
		assertEquals("abae2255-4255-4304-8589-737cdff61640", testResult2[1]);
		assertEquals("A", testResult2[3]);
	}

}
