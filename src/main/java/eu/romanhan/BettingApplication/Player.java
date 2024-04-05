package eu.romanhan.BettingApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.romanhan.BettingApplication.exceptions.IllegalWithdrawAmountException;
import eu.romanhan.BettingApplication.exceptions.InsufficientFundsException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Player {

	private UUID playerId;
	private long balance;
	private int totalBets;
	private int totalWins;
	private boolean hasIllegalAction = false;
	private List<String[]> actionData;

	public Player(UUID playerId) {
		this.playerId = playerId;
		this.balance = 0;
		this.totalBets = 0;
		this.totalWins = 0;
		this.actionData = new ArrayList<String[]>();
	}

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
			throw new IllegalWithdrawAmountException("" + amount);
		} else {
			balance -= amount;
		}
	}

	public int placeBet(Match match, int amount, String side) {
		if (balance < amount) {
			throw new InsufficientFundsException("Illegal action! Insufficient funds for bet");
		}
		int winAmount = 0;
		balance -= amount;

		if (match.getResult().equals(side)) {
			winAmount = match.calculateWinnings(side, amount);
			balance += winAmount;
			totalWins++;

		} else if (match.getResult().equals("DRAW")) {
			totalBets++;
			balance += amount;
			return winAmount;
		} else {
			totalBets++;
			return amount;
		}
		totalBets++;

		return -winAmount;
	}

	public BigDecimal calculateWinRate() {
		if (totalBets == 0 || totalWins == 0) {
			return new BigDecimal(0.0);
		}
		return BigDecimal.valueOf((double) totalWins / totalBets).setScale(2, RoundingMode.HALF_UP);
	}

}
