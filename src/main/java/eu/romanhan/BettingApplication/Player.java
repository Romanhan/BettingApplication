package eu.romanhan.BettingApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

	private UUID playerId;
	private long balance;
	private int totalBets;
	private int totalWins;
	private boolean hasIllegalAction = false;

	public void deposit(long amount) {
		if (amount < 0) {
			hasIllegalAction = true;
			throw new IllegalStateException("Illegal action! Cannot deposit a negative amount");
		} else {
			balance += amount;
		}
	}

	public void withdraw(long amount) {
		if (amount > balance) {
			hasIllegalAction = true;
			throw new IllegalStateException("Illegal action! Cannot withdrsw more than balance");
		} else {
			balance -= amount;
		}
	}

	public void placeBet(Match match, String side, long amount) {
		if (balance < amount) {
			hasIllegalAction = true;
			throw new IllegalStateException("Illegal action! Insufficient funds for bet");
		}
		balance -= amount;

		if (side == null || side.isEmpty()) {
			return;
		}
		if (match.getResult().equals(side)) {
			balance += amount * match.calculateWinnings(side, amount);
			totalWins++;
		}
		totalBets++;
	}

	public BigDecimal calculateWinRate() {
		if (totalBets == 0 || totalWins == 0) {
			return new BigDecimal(0.0);
		}
		return BigDecimal.valueOf((double) totalWins / totalBets).setScale(2, RoundingMode.HALF_UP);
	}
}
