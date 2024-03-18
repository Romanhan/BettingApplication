package eu.romanhan.BettingApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PlayerTest {

	@Test
	void testDeposit() {
		Player player = new Player();
		player.deposit(50);

		assertEquals(50, player.getBalance());
	}

	@Test
	void testDepositIllegalAmount() {
		Player player = new Player();

		assertThrows(IllegalStateException.class, () -> player.deposit(-50),
				"Cannot deposit a negative amount message expected");
		assertEquals(true, player.isHasIllegalAction());
	}

	@Test
	void testWithdraw() {
		Player player = new Player();
		player.deposit(50);
		player.withdraw(25);

		assertEquals(25, player.getBalance());
	}

	@Test
	void testWithdrawWhenInsufficientFunds() {
		Player player = new Player();
		player.deposit(50);

		assertThrows(IllegalStateException.class, () -> player.withdraw(60), "Insufficient funds message expected");
		assertEquals(true, player.isHasIllegalAction());
	}

	@Test
	void testPlaceBet() {
		Match match = new Match();
		match.setResult("A");

		Player player = new Player();
		player.setTotalBets(5);
		player.setTotalWins(1);

		player.setBalance(50);
		player.placeBet(match, 10, "A");

		assertEquals(6, player.getTotalBets());
		assertEquals(40, player.getBalance());
		assertEquals(2, player.getTotalWins());
	}

	@Test
	void testPlaceBetIllegalAction() {
		Match match = new Match();
		Player player = new Player();
		player.setBalance(50);

		assertThrows(IllegalStateException.class, () -> player.placeBet(match, 60, "A"));
	}

	@Test
	void testCalculateWinRate() {
		Player player = new Player();
		player.setTotalBets(2);
		player.setTotalWins(5);
		BigDecimal winRate = player.calculateWinRate();

		assertEquals(BigDecimal.valueOf(2.50).setScale(winRate.scale()), winRate);
	}

	@Test
	void testCalculateWinRateIfBetsOrWinsIsZero() {
		Player player = new Player();
		player.setTotalBets(0);
		player.setTotalWins(0);
		BigDecimal winRate = player.calculateWinRate();

		assertEquals(BigDecimal.valueOf(0.00).setScale(winRate.scale()), winRate);
	}

}
