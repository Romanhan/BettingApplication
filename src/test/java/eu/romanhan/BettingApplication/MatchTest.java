package eu.romanhan.BettingApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MatchTest {

	@Test
	void testCalculateWinningsSideA() {
		Match match = new Match();
		match.setRateA(1.5);
		match.setResult("A");
		int amount = 50;

		assertEquals(75, match.calculateWinnings("A", amount));
	}

	@Test
	void testCalculateWinningsSideB() {
		Match match = new Match();
		match.setRateB(2);
		match.setResult("B");
		int amount = 50;

		assertEquals(100, match.calculateWinnings("B", amount));
	}

	@Test
	void testCalculateWinningsNoSide() {
		Match match = new Match();
		match.setResult("DRAW");
		int amount = 50;

		assertEquals(50, match.calculateWinnings("A", amount));
	}

}
